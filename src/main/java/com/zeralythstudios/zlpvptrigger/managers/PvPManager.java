package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.api.PvPService;
import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import com.zeralythstudios.zlpvptrigger.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PvPManager implements PvPService {

    private final ZlPvPTrigger plugin;
    private final Set<UUID> enabled = new HashSet<>();
    private final Map<UUID, ItemStack[]> oldArmor = new HashMap<>();
    private final Map<UUID, Boolean> previousAllowFlight = new HashMap<>();
    private final Map<UUID, Boolean> previousFlying = new HashMap<>();

    public PvPManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    public boolean isHoldingTrigger(Player player) {
        return plugin.getTriggerItemManager().isTriggerItem(player.getInventory().getItemInMainHand());
    }

    public void evaluateState(Player player) {
        if (!plugin.getConfigManager().isSystemEnabled()) {
            plugin.getTimerManager().cancelAllFor(player.getUniqueId());
            if (isPvPEnabled(player.getUniqueId())) {
                disablePvP(player);
            }
            return;
        }

        if (isHoldingTrigger(player)) {
            plugin.getTimerManager().cancelDeactivation(player.getUniqueId());
            if (!isPvPEnabled(player.getUniqueId())) {
                plugin.getTimerManager().startActivation(player);
            }
        } else {
            plugin.getTimerManager().cancelActivation(player.getUniqueId());
            if (isPvPEnabled(player.getUniqueId())) {
                plugin.getTimerManager().startDeactivation(player);
            }
        }
    }

    public void enablePvP(Player player) {
        UUID uuid = player.getUniqueId();
        if (!enabled.add(uuid)) {
            return;
        }

        previousAllowFlight.put(uuid, Boolean.valueOf(player.getAllowFlight()));
        previousFlying.put(uuid, Boolean.valueOf(player.isFlying()));

        enforceNoFlight(player);
        equipKit(player);
        plugin.getMessageManager().send(player, "pvp.activation.enabled");
    }

    public void disablePvP(Player player) {
        UUID uuid = player.getUniqueId();
        if (!enabled.remove(uuid)) {
            return;
        }

        restoreFlightState(player);
        restoreKit(player);
        plugin.getMessageManager().send(player, "pvp.deactivation.disabled");
    }

    public void enforceNoFlight(Player player) {
        if (player.isGliding()) {
            player.setGliding(false);
        }
        if (player.isFlying()) {
            player.setFlying(false);
        }
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
        }
    }

    private void restoreFlightState(Player player) {
        UUID uuid = player.getUniqueId();
        Boolean allow = previousAllowFlight.remove(uuid);
        Boolean flying = previousFlying.remove(uuid);

        if (allow != null) {
            player.setAllowFlight(allow.booleanValue());
        }
        if (flying != null && flying.booleanValue()) {
            player.setFlying(true);
        }
    }

    @Override
    public boolean isPvPEnabled(UUID playerId) {
        return enabled.contains(playerId);
    }

    public void cleanupPlayer(UUID uuid) {
        enabled.remove(uuid);
        oldArmor.remove(uuid);
        previousAllowFlight.remove(uuid);
        previousFlying.remove(uuid);
        plugin.getTimerManager().cancelAllFor(uuid);
    }

    public void disableAllOnline() {
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (isPvPEnabled(online.getUniqueId())) {
                disablePvP(online);
            }
            plugin.getTimerManager().cancelAllFor(online.getUniqueId());
        }
    }

    public void cleanupAll() {
        enabled.clear();
        oldArmor.clear();
        previousAllowFlight.clear();
        previousFlying.clear();
    }

    private void equipKit(Player player) {
        if (!plugin.getConfigManager().isKitEnabled()) {
            return;
        }
        UUID uuid = player.getUniqueId();
        oldArmor.put(uuid, player.getInventory().getArmorContents().clone());
        player.getInventory().setHelmet(ItemUtils.createUnbreakableItem(plugin.getConfigManager().getArmorPiece("helmet", Material.NETHERITE_HELMET)));
        player.getInventory().setChestplate(ItemUtils.createUnbreakableItem(plugin.getConfigManager().getArmorPiece("chestplate", Material.NETHERITE_CHESTPLATE)));
        player.getInventory().setLeggings(ItemUtils.createUnbreakableItem(plugin.getConfigManager().getArmorPiece("leggings", Material.NETHERITE_LEGGINGS)));
        player.getInventory().setBoots(ItemUtils.createUnbreakableItem(plugin.getConfigManager().getArmorPiece("boots", Material.NETHERITE_BOOTS)));
    }

    private void restoreKit(Player player) {
        ItemStack[] armor = oldArmor.remove(player.getUniqueId());
        if (armor != null) {
            player.getInventory().setArmorContents(armor);
        }
    }
}
