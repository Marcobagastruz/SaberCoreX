package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.entity.Blaze;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiWaterBlaze extends SaberPlugin {
    public AntiWaterBlaze(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiWaterBlazeListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiWaterBlazeListener extends SaberPluginListener<AntiWaterBlaze> {

        public AntiWaterBlazeListener(AntiWaterBlaze patch) {
            super(patch);
        }

        @EventHandler
        public void onBlazeDamage(EntityDamageEvent e) {
            if (e.getEntity() instanceof Blaze && e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                e.setCancelled(true);
            }
        }
    }
}
