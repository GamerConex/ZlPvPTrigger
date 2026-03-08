package com.zeralythstudios.zlpvptrigger.listeners;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final ZlPvPTrigger plugin;

    public PlayerListener(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeldChange(PlayerItemHeldEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, () -> plugin.getPvPManager().evaluateState(event.getPlayer()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        plugin.getPvPManager().evaluateState(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int slot = plugin.getConfigManager().getTriggerSlot();
        ItemStack item = player.getInventory().getItem(slot);
        if (plugin.getTriggerItemManager().isTriggerItem(item)) {
            event.getDrops().removeIf(drop -> plugin.getTriggerItemManager().isTriggerItem(drop));
        }
        plugin.getPvPManager().cleanupPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPvPManager().isPvPEnabled(player.getUniqueId())) {
            plugin.getPvPManager().disablePvP(player);
        }
    }
}
