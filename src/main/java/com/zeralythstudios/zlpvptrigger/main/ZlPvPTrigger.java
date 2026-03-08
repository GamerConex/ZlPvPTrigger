package com.zeralythstudios.zlpvptrigger.main;

import com.zeralythstudios.zlpvptrigger.listeners.CombatListener;
import com.zeralythstudios.zlpvptrigger.listeners.ConnectionListener;
import com.zeralythstudios.zlpvptrigger.listeners.FlightListener;
import com.zeralythstudios.zlpvptrigger.listeners.InventoryListener;
import com.zeralythstudios.zlpvptrigger.listeners.PlayerListener;
import com.zeralythstudios.zlpvptrigger.managers.CombatManager;
import com.zeralythstudios.zlpvptrigger.managers.ConfigManager;
import com.zeralythstudios.zlpvptrigger.managers.InventoryProtectionManager;
import com.zeralythstudios.zlpvptrigger.managers.MessageManager;
import com.zeralythstudios.zlpvptrigger.managers.PvPManager;
import com.zeralythstudios.zlpvptrigger.managers.TimerManager;
import com.zeralythstudios.zlpvptrigger.managers.TriggerItemManager;
import com.zeralythstudios.zlpvptrigger.managers.UpdateChecker;
import com.zeralythstudios.zlpvptrigger.tasks.UpdateCheckTask;
import com.zeralythstudios.zlpvptrigger.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ZlPvPTrigger extends JavaPlugin {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private TriggerItemManager triggerItemManager;
    private TimerManager timerManager;
    private PvPManager pvPManager;
    private InventoryProtectionManager inventoryProtectionManager;
    private CombatManager combatManager;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.triggerItemManager = new TriggerItemManager(this);
        this.timerManager = new TimerManager(this);
        this.pvPManager = new PvPManager(this);
        this.inventoryProtectionManager = new InventoryProtectionManager(this);
        this.combatManager = new CombatManager(this);
        this.updateChecker = new UpdateChecker(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CombatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FlightListener(this), this);

        ZlPvPTriggerCommand commandHandler = new ZlPvPTriggerCommand(this);
        PluginCommand command = getCommand("zlpvptrigger");
        if (command != null) {
            command.setExecutor(commandHandler);
            command.setTabCompleter(commandHandler);
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, new UpdateCheckTask(updateChecker));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&a[ZlPvPTrigger] Plugin enabled successfully. Thank you for using it! &7(Created by Zeralyth Studios)"));
    }

    @Override
    public void onDisable() {
        timerManager.cancelAll();
        pvPManager.cleanupAll();
    }

    public void reloadAll() {
        configManager.reload();
        messageManager.reload();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public TriggerItemManager getTriggerItemManager() {
        return triggerItemManager;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }

    public PvPManager getPvPManager() {
        return pvPManager;
    }

    public InventoryProtectionManager getInventoryProtectionManager() {
        return inventoryProtectionManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }
}
