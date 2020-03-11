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
public class CommandSaberUnregister implements CommandExecutor {
    private SaberCore plugin;

    public CommandSaberUnregister(Plugin p) {
        this.plugin = (SaberCore) p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        } else if (args.length != 1) {
            sender.sendMessage("Usage: /saberunregister <module>");
            sender.sendMessage("Loaded Modules: [" + SaberPlugins.getLoadedPatches() + "]");
            return true;
        } else {
            String patch = args[0];
            if (this.plugin.unregisterPatch(patch)) {
                if (sender instanceof Player) sender.sendMessage("Unloaded modules." + patch.toLowerCase());
                this.plugin.getConfig().set("modules." + patch.toLowerCase(), false);
            } else {
                sender.sendMessage("No module with surname '" + patch + "' is loaded!");
            }
            return true;
        }
    }
}
