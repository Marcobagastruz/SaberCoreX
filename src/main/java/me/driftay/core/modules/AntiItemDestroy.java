package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiItemDestroy extends SaberPlugin {
    private static List<String> lightningDeny = SaberCore.getInstance().getConfig().getStringList("settings.lightning-deny");
    private static List<String> explosionDeny = SaberCore.getInstance().getConfig().getStringList("settings.explosion-deny");
    private static List<String> lavaBurnDeny = SaberCore.getInstance().getConfig().getStringList("settings.burn-deny");


    public AntiItemDestroy(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiItemDestroyListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiItemDestroyListener extends SaberPluginListener<AntiItemDestroy> {

        public AntiItemDestroyListener(AntiItemDestroy patch) {
            super(patch);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        private void onEntityDamage(EntityDamageByEntityEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) {
                return;
            }
            if (!(e.getEntity() instanceof Item)) {
                return;
            }
            Material type = ((Item) e.getEntity()).getItemStack().getType();
            if (!lightningDeny.contains(type.name())) {
                return;
            }
            e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private void onEventLava(EntityDamageEvent e) {
            if (e.getEntity().getType() != EntityType.DROPPED_ITEM) return;

            if (!(e.getEntity() instanceof Item)) return;

            if (e.getCause() != EntityDamageEvent.DamageCause.LAVA && e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK)
                return;

            Material type = ((Item) e.getEntity()).getItemStack().getType();
            if (!lavaBurnDeny.contains(type.name())) return;
            e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private void onEventExplosion(EntityDamageEvent e) {
            if (e.getEntity().getType() != EntityType.DROPPED_ITEM) return;

            if (!(e.getEntity() instanceof Item)) return;

            if (e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                return;

            Material type = ((Item) e.getEntity()).getItemStack().getType();
            if (!explosionDeny.contains(type.name())) return;

            e.setCancelled(true);
        }
    }
}

