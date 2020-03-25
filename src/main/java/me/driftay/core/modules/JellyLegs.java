package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/25/2020
 */
public class JellyLegs extends SaberPlugin {

    public static Set<UUID> jellyLegs = new HashSet<>();

    public JellyLegs(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerCommand(new JellyLegsCommand(this));
        this.registerListener(new JellyLegsListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterCommands();
        this.unregisterListeners();
    }

    static class JellyLegsListener extends SaberPluginListener<JellyLegs> {

        public JellyLegsListener(JellyLegs patch) {
            super(patch);
        }

        @EventHandler
        public void onFall(EntityDamageEvent event) {
            if (event.getCause() != EntityDamageEvent.DamageCause.FALL || event.getEntity().getType() != EntityType.PLAYER)
                return;

            if (jellyLegs.contains(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }


    static class JellyLegsCommand extends SaberPluginCommand<JellyLegs> {
        public JellyLegsCommand(JellyLegs patch) {
            super(patch, "jellylegs");
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("sabercore.jellylegs")) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.must-be-player")));
                return true;
            }

            if (jellyLegs.contains(((Player) sender).getUniqueId())) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("jelly-legs.toggled-off")));
                jellyLegs.remove(sender.getName());
                return true;
            }

            sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("jelly-legs.toggled-on")));
            jellyLegs.add(((Player) sender).getUniqueId());

            return true;
        }
    }
}
