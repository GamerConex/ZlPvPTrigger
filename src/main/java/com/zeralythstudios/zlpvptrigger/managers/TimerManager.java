package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerManager {

    private final ZlPvPTrigger plugin;
    private final Map<UUID, BukkitTask> activationTasks = new HashMap<>();
    private final Map<UUID, BukkitTask> deactivationTasks = new HashMap<>();

    public TimerManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    public void startActivation(Player player) {
        UUID uuid = player.getUniqueId();
        if (activationTasks.containsKey(uuid) || plugin.getPvPManager().isPvPEnabled(uuid) || !plugin.getConfigManager().isSystemEnabled()) {
            return;
        }
        cancelDeactivation(uuid);

        BukkitTask task = new BukkitRunnable() {
            int secondsLeft = plugin.getConfigManager().getActivationDelay();

            @Override
            public void run() {
                if (!player.isOnline()) {
                    activationTasks.remove(uuid);
                    cancel();
                    return;
                }
                if (!plugin.getConfigManager().isSystemEnabled() || !plugin.getPvPManager().isHoldingTrigger(player)) {
                    activationTasks.remove(uuid);
                    cancel();
                    return;
                }

                plugin.getMessageManager().send(player, "pvp.activation.countdown",
                        java.util.Collections.singletonMap("seconds", String.valueOf(secondsLeft)));

                if (secondsLeft <= 0) {
                    activationTasks.remove(uuid);
                    plugin.getPvPManager().enablePvP(player);
                    cancel();
                    return;
                }
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        activationTasks.put(uuid, task);
    }

    public void startDeactivation(Player player) {
        UUID uuid = player.getUniqueId();
        if (deactivationTasks.containsKey(uuid) || !plugin.getPvPManager().isPvPEnabled(uuid)) {
            return;
        }
        cancelActivation(uuid);

        BukkitTask task = new BukkitRunnable() {
            int secondsLeft = plugin.getConfigManager().getDeactivationDelay();

            @Override
            public void run() {
                if (!player.isOnline()) {
                    deactivationTasks.remove(uuid);
                    cancel();
                    return;
                }
                if (plugin.getPvPManager().isHoldingTrigger(player)) {
                    deactivationTasks.remove(uuid);
                    cancel();
                    return;
                }

                plugin.getMessageManager().send(player, "pvp.deactivation.countdown",
                        java.util.Collections.singletonMap("seconds", String.valueOf(secondsLeft)));

                if (secondsLeft <= 0) {
                    deactivationTasks.remove(uuid);
                    plugin.getPvPManager().disablePvP(player);
                    cancel();
                    return;
                }
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        deactivationTasks.put(uuid, task);
    }

    public void cancelActivation(UUID uuid) {
        BukkitTask task = activationTasks.remove(uuid);
        if (task != null) {
            task.cancel();
        }
    }

    public void cancelDeactivation(UUID uuid) {
        BukkitTask task = deactivationTasks.remove(uuid);
        if (task != null) {
            task.cancel();
        }
    }

    public void cancelAllFor(UUID uuid) {
        cancelActivation(uuid);
        cancelDeactivation(uuid);
    }

    public void cancelAll() {
        for (BukkitTask task : activationTasks.values()) {
            task.cancel();
        }
        for (BukkitTask task : deactivationTasks.values()) {
            task.cancel();
        }
        activationTasks.clear();
        deactivationTasks.clear();
    }
}
