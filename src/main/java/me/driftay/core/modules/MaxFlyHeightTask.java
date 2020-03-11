package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/11/2020
 */

public class MaxFlyHeightTask extends BukkitRunnable {
    public void run() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getLocation().getY() > SaberCore.getInstance().getConfig().getDouble("settings.max-fly-height.limit") && p.isFlying() && !p.isOp()) {
                Location locFixed = p.getLocation();
                locFixed.setY(255.0);
                if (!locFixed.getBlock().getType().isBlock()) {
                    p.teleport(locFixed, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                    p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("max-fly-height.limit").replace("{limit}", SaberCore.getInstance().getConfig().getDouble("settings.max-fly-height.limit") + "")));
                } else {
                    p.setFlying(false);
                }
            } else {
                if (p.getLocation().getY() >= 0.0 || !p.isFlying() || p.isOp()) continue;
                p.setFlying(false);
                p.setAllowFlight(false);
                if(SaberCore.getInstance().getConfig().getBoolean("settings.max-fly-height.damage-player")) {
                    p.damage(1.0);
                }
            }
        }
    }
}
