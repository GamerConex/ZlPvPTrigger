package com.zeralythstudios.zlpvptrigger.utils;

import net.md_5.bungee.api.ChatColor;

public final class ColorUtils {

    private ColorUtils() {
    }

    public static String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
