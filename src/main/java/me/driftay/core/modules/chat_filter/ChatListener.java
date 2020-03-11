package me.driftay.core.modules.chat_filter;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.ChatMode;
import me.driftay.core.SaberCore;
import me.driftay.core.utils.Cooldown;
import me.driftay.core.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if (fPlayer.getChatMode() == ChatMode.ALLIANCE
                || fPlayer.getChatMode() == ChatMode.FACTION
                || fPlayer.getChatMode() == ChatMode.MOD
                || fPlayer.getChatMode() == ChatMode.TRUCE) return;

        if (ChatHandler.chatMuted && !player.hasPermission("sabercore.staff")) {
            player.sendMessage(ChatColor.RED + "Public chat is currently muted.");
            e.setCancelled(true);
            return;
        }

        int timeLeft = Cooldown.getTimeLeft(player.getUniqueId(), "slowchat");
        if (!player.hasPermission("sabercore.chatdelay.bypass")) {
            if (Cooldown.isInCooldown(player.getUniqueId(), "slowchat")) {
                player.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("chat-filter.chat-cooldown").replace("{time}", timeLeft + "")));
                e.setCancelled(true);
                return;
            } else {
                Cooldown c = new Cooldown(player.getUniqueId(), "slowchat", ChatHandler.delayTime);
                c.start();
            }
        }

        if (!player.hasPermission("sabercore.chatfilter.bypass")) {
            if (SaberCore.getInstance().getChatHandler().shouldFilter(e.getMessage())) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Your message was filtered.");
                messageStaff(formatFilteredPublicMessage(player, e.getMessage()));
            }
        }
    }

    public String formatFilteredPublicMessage(Player player, String message) {
        return StringUtils.translate(SaberCore.getInstance().getConfig().getString("settings.chat-filter.format")).replace("{player}", player.getName()).replace("{message}", message);
    }

    public void messageStaff(String message) {
        for (Player player : SaberCore.getInstance().getServer().getOnlinePlayers()) {
            if (player.hasPermission("sabercore.staff")) {
                player.sendMessage(message);
            }
        }
    }
}
