package me.driftay.core.commands;

import me.driftay.core.SaberCore;
import me.driftay.core.plugins.SaberPlugins;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class CommandSaberReload implements CommandExecutor {
    private SaberCore plugin;

    public CommandSaberReload(Plugin p) {
        this.plugin = (SaberCore) p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        } else if (args.length != 1) {
            sender.sendMessage("Usage: /saberload <module or *>");
            sender.sendMessage("Loaded Modules: [" + SaberPlugins.getLoadedPatches() + "]");
            return true;
        } else {
            String patch = args[0];
            if (patch.equals("*")) {
                this.plugin.reloadConfig();
                for (String s : this.plugin.getConfig().getKeys(true)) {
                    if (s.startsWith("modules.")) {
                        Bukkit.getLogger().info(s + ": " + this.plugin.getConfig().getBoolean(s));
                    }
                }

                sender.sendMessage("Configuration reloaded.");
                sender.sendMessage("Killing modules...");
                this.plugin.unregisterPatches();
                sender.sendMessage("Injecting modules...");
                this.plugin.registerPatches();
            } else {
                sender.sendMessage("Killing module...");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "saberunregister " + patch);
                sender.sendMessage("Injecting module...");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "saberregister " + patch);
                sender.sendMessage("Done!");
            }

            return true;
        }
    }
}
