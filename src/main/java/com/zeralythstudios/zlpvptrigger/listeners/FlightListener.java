package com.zeralythstudios.zlpvptrigger.listeners;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlightListener implements Listener {

    private final ZlPvPTrigger plugin;

    public FlightListener(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        if (!plugin.getPvPManager().isPvPEnabled(event.getPlayer().getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        plugin.getPvPManager().enforceNoFlight(event.getPlayer());
    }

    @EventHandler
    public void onToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!plugin.getPvPManager().isPvPEnabled(player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        plugin.getPvPManager().enforceNoFlight(player);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getPvPManager().isPvPEnabled(event.getPlayer().getUniqueId())) {
            return;
        }
        plugin.getPvPManager().enforceNoFlight(event.getPlayer());
    }
}
