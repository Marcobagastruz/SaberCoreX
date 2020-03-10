package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AutoRespawn extends SaberPlugin {
    public AutoRespawn(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AutoRespawnListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AutoRespawnListener extends SaberPluginListener<AutoRespawn> {

        public AutoRespawnListener(AutoRespawn patch) {
            super(patch);
        }

        @EventHandler
        private void onPlayerDeath(PlayerDeathEvent event) {
            Player player = event.getEntity();
            Bukkit.getScheduler().runTaskLater(SaberCore.getInstance(), () -> player.spigot().respawn(), 5L);
        }
    }
}
