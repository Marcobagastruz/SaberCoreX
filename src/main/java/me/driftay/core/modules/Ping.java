package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/25/2020
 */
public class Ping extends SaberPlugin {
    public Ping(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerCommand(new PingCommand(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterCommands();
    }

    static class PingCommand extends SaberPluginCommand<Ping> {

        public PingCommand(Ping patch) {
            super(patch, "ping");
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            Player p = (Player) sender;
            if (sender instanceof Player) {
                if (args.length == 1) {
                    if (!p.hasPermission("sabercore.ping.other")) {
                        p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (Bukkit.getPlayer(args[0]) != null) {
                        p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("ping.ping-other")).replace("{player}", target.getName()).replace("{ping}", String.valueOf(StringUtils.getPing(target))));
                    } else {
                        p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.player-not-found")).replace("{player}", target.getName()));
                    }
                } else {
                    p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("ping.ping-self").replace("{ping}", String.valueOf(StringUtils.getPing(p))).replace("{player}", sender.getName())));
                }
            }
            return false;
        }
    }
}
