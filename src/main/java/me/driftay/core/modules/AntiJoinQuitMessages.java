package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiJoinQuitMessages extends SaberPlugin {
    public AntiJoinQuitMessages(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiJoinQuitMessagesListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiJoinQuitMessagesListener extends SaberPluginListener<AntiJoinQuitMessages> {

        public AntiJoinQuitMessagesListener(AntiJoinQuitMessages patch) {
            super(patch);
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            e.setJoinMessage(null);
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            e.setQuitMessage(null);
        }
    }
}
