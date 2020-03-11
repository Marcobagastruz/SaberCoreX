package me.driftay.core.modules.chat_filter;

import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class ChatFilter extends SaberPlugin {
    public ChatFilter(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new ChatListener());
        this.registerCommand(new CommandChat("chat", "/<command>", "Chat toggling and clearing.", new ArrayList<>(Collections.singletonList("chatfilter"))));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
        ChatHandler.chatMuted = false;
    }
}
