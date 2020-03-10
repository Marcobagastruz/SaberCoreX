package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiMobAI extends SaberPlugin {
    public AntiMobAI(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiMobAIListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiMobAIListener extends SaberPluginListener<AntiMobAI> {

        public AntiMobAIListener(AntiMobAI patch) {
            super(patch);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityTarget(EntityTargetEvent event){
            event.setCancelled(true);
        }
    }
}
