package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiMobMovement extends SaberPlugin {
    public static List<String> entList = SaberCore.getInstance().getConfig().getStringList("settings.anti-mob-movement");

    public AntiMobMovement(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiMobMovementListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiMobMovementListener extends SaberPluginListener<AntiMobMovement> {

        public AntiMobMovementListener(AntiMobMovement patch) {
            super(patch);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void entitySpawnEvent(EntitySpawnEvent event) {
            if (entList.isEmpty()) return;
            if (event.getEntity().getType() == EntityType.DROPPED_ITEM || event.getEntity().getType() == EntityType.PRIMED_TNT)
                return;
            if (event.getEntity() instanceof Player) return;

            if (!entList.contains(event.getEntity().getType().toString())) return;
            final LivingEntity entity = (LivingEntity) event.getEntity();
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 25));
        }

    }
}
