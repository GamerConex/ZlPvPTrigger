package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final ZlPvPTrigger plugin;
    private boolean systemEnabled;

    public ConfigManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
        this.systemEnabled = plugin.getConfig().getBoolean("system-enabled", true);
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.systemEnabled = plugin.getConfig().getBoolean("system-enabled", true);
    }

    public boolean isSystemEnabled() {
        return systemEnabled;
    }

    public void setSystemEnabled(boolean enabled) {
        this.systemEnabled = enabled;
    }

    public Material getTriggerMaterial() {
        String configured = getConfig().getString("pvp-trigger-item", "DIAMOND_SWORD");
        Material material = Material.matchMaterial(configured);
        return material == null ? Material.DIAMOND_SWORD : material;
    }

    public int getTriggerSlot() {
        return Math.max(0, Math.min(8, getConfig().getInt("trigger-slot", 0)));
    }

    public int getActivationDelay() {
        return Math.max(1, getConfig().getInt("activation-delay", 5));
    }

    public int getDeactivationDelay() {
        return Math.max(1, getConfig().getInt("deactivation-delay", 5));
    }

    public boolean isKitEnabled() {
        return getConfig().getBoolean("pvp-kit.enabled", true);
    }

    public Material getArmorPiece(String piece, Material fallback) {
        String configured = getConfig().getString("pvp-kit.armor." + piece, fallback.name());
        Material material = Material.matchMaterial(configured);
        return material == null ? fallback : material;
    }
}
