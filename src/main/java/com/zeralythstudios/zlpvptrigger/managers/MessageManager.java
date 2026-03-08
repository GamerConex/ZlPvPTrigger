package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import com.zeralythstudios.zlpvptrigger.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class MessageManager {

    private final ZlPvPTrigger plugin;
    private FileConfiguration messages;

    public MessageManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.messages = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, Map<String, String> placeholders) {
        String prefix = messages.getString("general.prefix", "&7[ZlPvPTrigger] ");
        String raw = messages.getString(key, "%prefix%&cMissing message: " + key);
        String output = raw.replace("%prefix%", prefix);

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                output = output.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }
        return MessageUtils.color(output);
    }

    public void send(CommandSender sender, String key) {
        sender.sendMessage(get(key));
    }

    public void send(CommandSender sender, String key, Map<String, String> placeholders) {
        sender.sendMessage(get(key, placeholders));
    }
}
