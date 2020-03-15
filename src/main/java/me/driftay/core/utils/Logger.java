package me.driftay.core.utils;

import me.driftay.core.SaberCore;
import org.bukkit.ChatColor;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/15/2020
 */
public class Logger {

    public static void print(String message, PrefixType type) {
        SaberCore.instance.getServer().getConsoleSender().sendMessage(type.getPrefix() + message);
    }

    public enum PrefixType {

        WARNING(ChatColor.RED + "WARNING: "), NONE(""), DEFAULT(ChatColor.GOLD + "[SaberCoreX] "), FAILED(ChatColor.RED + "FAILED: ");

        private String prefix;

        PrefixType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }

    }

}
