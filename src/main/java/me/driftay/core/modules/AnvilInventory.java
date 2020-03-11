package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.Collections;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AnvilInventory extends SaberPlugin {
    public AnvilInventory(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        registerCommand(new CommandAnvil(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
        this.unregisterCommands();
    }


    static class CommandAnvil extends SaberPluginCommand<AnvilInventory> {

        public CommandAnvil(AnvilInventory patch) {
            super(patch, "anvil");
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) return false;


            if (!sender.hasPermission("sabercore.anvil")) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                return false;
            }

            Player player = (Player) sender;
            player.closeInventory();
            player.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));
            return false;
        }
    }
}
