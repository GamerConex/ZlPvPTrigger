package com.zeralythstudios.zlpvptrigger.main;

import com.zeralythstudios.zlpvptrigger.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ZlPvPTriggerCommand implements CommandExecutor, TabCompleter {

    private final ZlPvPTrigger plugin;
    private static final List<String> SUBCOMMANDS = Arrays.asList("reload", "help", "support", "version", "off", "on");

    public ZlPvPTriggerCommand(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            plugin.getMessageManager().send(sender, "commands.help.header");
            plugin.getMessageManager().send(sender, "commands.help.line-1");
            plugin.getMessageManager().send(sender, "commands.help.line-2");
            plugin.getMessageManager().send(sender, "commands.help.line-3");
            plugin.getMessageManager().send(sender, "commands.help.line-4");
            plugin.getMessageManager().send(sender, "commands.help.line-5");
            plugin.getMessageManager().send(sender, "commands.help.line-6");
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("version")) {
            sender.sendMessage(plugin.getMessageManager().get("general.prefix")
                    + MessageUtils.color("&7Current version: &f" + plugin.getDescription().getVersion()));
            return true;
        }
        if (sub.equals("support")) {
            sender.sendMessage(plugin.getMessageManager().get("general.prefix")
                    + MessageUtils.color("&bSupport: discord.gg/Y6HGkgW2YS"));
            return true;
        }

        if (!sender.hasPermission("zlpvptrigger.admin")) {
            plugin.getMessageManager().send(sender, "commands.no-permission");
            return true;
        }

        if (sub.equals("reload")) {
            plugin.reloadAll();
            plugin.getMessageManager().send(sender, "commands.reload");
            return true;
        }

        if (sub.equals("off")) {
            plugin.getConfigManager().setSystemEnabled(false);
            plugin.getPvPManager().disableAllOnline();
            plugin.getMessageManager().send(sender, "commands.system-disabled");
            return true;
        }

        if (sub.equals("on")) {
            plugin.getConfigManager().setSystemEnabled(true);
            plugin.getMessageManager().send(sender, "commands.system-enabled");
            return true;
        }

        plugin.getMessageManager().send(sender, "commands.unknown");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> out = new ArrayList<String>();
            String start = args[0].toLowerCase();
            for (String sub : SUBCOMMANDS) {
                if (sub.startsWith(start)) {
                    out.add(sub);
                }
            }
            return out;
        }
        return Collections.emptyList();
    }
}
