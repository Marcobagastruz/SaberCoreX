package me.driftay.core.modules.chat_filter;

import me.driftay.core.SaberCore;
import me.driftay.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class ChatHandler {

    public static int delayTime = SaberCore.getInstance().getConfig().getInt("settings.chat-filter.chat-slow-time");
    public static boolean chatMuted = false;
    private final Pattern URL_REGEX = Pattern.compile(
            "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
    private final Pattern IP_REGEX = Pattern.compile(
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    public static void toggleMuteChat() {
        chatMuted = !chatMuted;
    }

    protected static void clearChat(Player whoCleared) {
        String clearer = (whoCleared == null) ? "Console" : whoCleared.getName();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            for (int i = 0; i < 75; ++i) player.sendMessage(" ");
            SaberCore.getInstance().getFileManager().getMessages().fetchStringList("chat-filter.cleared-chat-broadcast").forEach(line -> player.sendMessage(StringUtils.translate(line).replace("{user}", clearer)));
        }
        if (whoCleared != null) whoCleared.sendMessage(ChatColor.GREEN + "You have cleared the chat.");
    }

    protected static void toggleChat(Player whoToggled) {
        String toggler = (whoToggled == null) ? "Console" : whoToggled.getName();
        if (!chatMuted) {
            toggleMuteChat();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                for (int i = 0; i < 75; ++i) player.sendMessage(" ");
                SaberCore.getInstance().getFileManager().getMessages().fetchStringList("chat-filter.disabled-chat-broadcast").forEach(line -> player.sendMessage(StringUtils.translate(line).replace("{user}", toggler)));
            }
            if (whoToggled != null) whoToggled.sendMessage(ChatColor.RED + "You have toggled the chat OFF.");
        } else {
            toggleMuteChat();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                for (int i = 0; i < 75; ++i) player.sendMessage(" ");
                SaberCore.getInstance().getFileManager().getMessages().fetchStringList("chat-filter.enabled-chat-broadcast").forEach(line -> player.sendMessage(StringUtils.translate(line).replace("{user}", toggler)));
            }
            if (whoToggled != null) whoToggled.sendMessage(ChatColor.GREEN + "You have toggled the chat ON.");
        }
    }

    public boolean shouldFilter(String message) {
        String msg = message.toLowerCase()
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("@", "a")
                .replace("7", "t")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replaceAll("\\p{Punct}|\\d", "").trim();

        String[] words = msg.trim().split(" ");
        for (String word : words) {
            for (String filteredWord : SaberCore.getInstance().getConfig().getStringList("settings.chat-filter.filtered-words")) {
                if (word.contains(filteredWord)) {
                    return true;
                }
            }
        }
        for (String word : message.replace("(dot)", ".").replace("[dot]", ".").trim().split(" ")) {
            boolean continueIt = false;
            for (String phrase : SaberCore.getInstance().getConfig().getStringList("settings.chat-filter.whitelisted-links")) {
                if (word.toLowerCase().contains(phrase)) {
                    continueIt = true;
                    break;
                }
            }
            if (continueIt) {
                continue;
            }
            Matcher matcher = IP_REGEX.matcher(word);
            if (matcher.matches()) {
                return true;
            }
            matcher = URL_REGEX.matcher(word);
            if (matcher.matches()) {
                return true;
            }
        }
        Optional<String> optional = SaberCore.getInstance().getConfig().getStringList("settings.chat-filter.filtered-phrases").stream().filter(msg::contains).findFirst();
        return optional.isPresent();
    }

}

