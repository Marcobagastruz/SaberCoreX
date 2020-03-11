package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiBabyZombie extends SaberPlugin {
    public AntiBabyZombie(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiBabyZombieListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiBabyZombieListener extends SaberPluginListener<AntiBabyZombie> {

        public AntiBabyZombieListener(AntiBabyZombie patch) {
            super(patch);
        }

        @EventHandler
        public void onEntitySpawn(CreatureSpawnEvent event) {
            if (event.getEntity().getType() == EntityType.ZOMBIE) {
                Zombie zombie = (Zombie) event.getEntity();
                if (zombie.isBaby())
                    event.setCancelled(true);
                if (zombie.isInsideVehicle())
                    event.setCancelled(true);
            }
        }
    }
}
