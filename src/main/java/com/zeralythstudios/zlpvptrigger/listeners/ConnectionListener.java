package com.zeralythstudios.zlpvptrigger.listeners;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final ZlPvPTrigger plugin;

    public ConnectionListener(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getTriggerItemManager().ensureTriggerItem(event.getPlayer());
        plugin.getPvPManager().evaluateState(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPvPManager().cleanupPlayer(event.getPlayer().getUniqueId());
    }
}
