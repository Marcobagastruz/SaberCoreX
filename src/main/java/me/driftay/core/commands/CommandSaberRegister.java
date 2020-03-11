package me.driftay.core.commands;

import me.driftay.core.SaberCore;
import me.driftay.core.plugins.SaberPlugins;
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
public class CommandSaberRegister implements CommandExecutor {
    private SaberCore plugin;

    public CommandSaberRegister(Plugin p) {
        this.plugin = (SaberCore) p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        } else if (args.length != 1) {
            sender.sendMessage("Usage: /saberregister <module>");
            sender.sendMessage("Unloaded Modules: [" + SaberPlugins.getUnloadedPatches() + "]");
            return true;
        } else {
            String patch = args[0];
            try {
                if (this.plugin.registerPatch(SaberPlugins.valueOf(patch.toUpperCase()).getSaberPlugin())) {
                    if (sender instanceof Player) {
                        sender.sendMessage("Loaded module." + patch.toLowerCase());
                    }
                    this.plugin.getConfig().set("modules." + patch.toLowerCase(), true);
                } else {
                    sender.sendMessage("Module '" + patch + "' is already loaded!");
                }
                return true;
            } catch (IllegalArgumentException e) {
                sender.sendMessage("No valid '" + patch + "' module!");
                return true;
            }
        }
    }
}
