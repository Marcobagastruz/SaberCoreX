package me.driftay.core.plugins;

import me.driftay.core.SaberCore;
import me.driftay.core.modules.*;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public enum SaberPlugins {
    ANTI_BABY_ZOMBIE(new AntiBabyZombie(SaberCore.getInstance()), "Disables The Spawning Of Baby Zombies"),
    ANTI_BOAT_MINECART(new AntiBoatMinecart(SaberCore.getInstance()), "Disables Placement of Boats and Minecarts"),
    ANTI_COBBLE_MONSTER(new AntiCobbleMonster(SaberCore.getInstance()), "Disable Cobble Monsters"),
    ANTI_CREEPER_GLITCH(new AntiCreeperGlitch(SaberCore.getInstance()), "Disables Glitching Of Creepers"),
    ANTI_EXPLOSION_DAMAGE(new AntiExplosionDamage(SaberCore.getInstance()), "Disables All Explosion Damage To Players"),
    ANTI_ITEM_DESTROY(new AntiItemDestroy(SaberCore.getInstance()), "Deny Items From Being Exploded/Burned"),
    ANTI_MOB_AI(new AntiMobAI(SaberCore.getInstance()), "Disables AI/Targeting of Mobs"),
    ANTI_MOB_MOVEMENT(new AntiMobMovement(SaberCore.getInstance()), "Disables Movement of Certain Entities"),
    ANTI_WILDERNESS_SPAWNER(new AntiWildernessSpawner(SaberCore.getInstance()), "Disable Spawners From Spawning In Wilderness"),
    AUTO_RESPAWN(new AutoRespawn(SaberCore.getInstance()), "Automatically Respawn");

    private SaberPlugin rp;
    private String desc;

    SaberPlugins(SaberPlugin rp, String desc) {
        this.rp = rp;
        this.desc = desc;
    }

    public static SaberPlugins reflectSaberPlugin(SaberPlugin ap) {
        for (SaberPlugins aps : values())
            if (aps.getSaberPlugin().getClass().getSimpleName().equals(ap.getClass().getSimpleName())) return aps;
        return null;
    }

    public static String getPatchList() {
        StringBuilder s = new StringBuilder();
        for (SaberPlugins aps : values()) s.append(aps.getConfigName()).append(", ");
        return s.toString();
    }

    public static String getUnloadedPatches() {
        StringBuilder s = new StringBuilder();
        for (SaberPlugins aps : values())
            if (!aps.getSaberPlugin().isRunning()) s.append(aps.getConfigName()).append(", ");
        return s.toString();
    }

    public static String getLoadedPatches() {
        StringBuilder s = new StringBuilder();
        for (SaberPlugins aps : values())
            if (aps.getSaberPlugin().isRunning()) s.append(aps.getConfigName()).append(", ");
        return s.toString();
    }

    public SaberPlugin getSaberPlugin() {
        return this.rp;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getConfigName() {
        return this.name().toLowerCase();
    }
}
