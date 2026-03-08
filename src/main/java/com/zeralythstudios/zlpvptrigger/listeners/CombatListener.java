package com.zeralythstudios.zlpvptrigger.listeners;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final ZlPvPTrigger plugin;

    public CombatListener(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isSystemEnabled()) {
            return;
        }

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();

        if (!plugin.getCombatManager().canFight(attacker, defender)) {
            event.setCancelled(true);
            plugin.getMessageManager().send(attacker, "combat.only-pvp-enabled-fight");
            return;
        }

        // Allows optional bypass when other plugins disabled PvP in region.
        event.setCancelled(false);
    }
}
