package me.driftay.core.modules.chat_filter;

import me.driftay.core.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class CommandChat extends AbstractCommand {
    public CommandChat(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player != null && !player.hasPermission("sabercore.staff")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /chat <clear|toggle>");
            return true;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            ChatHandler.clearChat(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            ChatHandler.toggleChat(player);
            return true;
        }
        sender.sendMessage(ChatColor.RED + " Usage: /chat <clear|toggle>");
        return true;
    }
}
