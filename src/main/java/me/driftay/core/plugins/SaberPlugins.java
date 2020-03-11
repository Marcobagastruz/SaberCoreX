package me.driftay.core.plugins;

import me.driftay.core.SaberCore;
import me.driftay.core.modules.*;
import me.driftay.core.modules.chat_filter.ChatFilter;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public enum SaberPlugins {

    ANTI_AUTO_FARM(new AntiAutoFarm(SaberCore.getInstance()), "Disable Piston AutoFarms"),
    ANTI_BABY_ZOMBIE(new AntiBabyZombie(SaberCore.getInstance()), "Disables The Spawning Of Baby Zombies"),
    ANTI_BOAT_MINECART(new AntiBoatMinecart(SaberCore.getInstance()), "Disables Placement of Boats and Minecarts"),
    ANTI_COBBLE_MONSTER(new AntiCobbleMonster(SaberCore.getInstance()), "Disable Cobble Monsters"),
    ANTI_CREEPER_GLITCH(new AntiCreeperGlitch(SaberCore.getInstance()), "Disables Glitching Of Creepers"),
    ANTI_DRAGONEGG_TELEPORT(new AntiDragonEggTeleport(SaberCore.getInstance()), "Disables ALL FORMS Of Teleportation For Dragon Eggs"),
    ANTI_EXPLOSION_DAMAGE(new AntiExplosionDamage(SaberCore.getInstance()), "Disables All Explosion Damage To Players"),
    ANTI_ITEM_DESTROY(new AntiItemDestroy(SaberCore.getInstance()), "Deny Items From Being Exploded/Burned"),
    ANTI_ITEM_STORAGE(new AntiItemStorage(SaberCore.getInstance()), "Deny Certain Items From Being Stored Anywhere"),
    ANTI_JOINQUIT_MESSAGES(new AntiJoinQuitMessages(SaberCore.getInstance()), "Disable All Forms Of Join Quit Messages"),
    ANTI_MOB_AI(new AntiMobAI(SaberCore.getInstance()), "Disables AI/Targeting of Mobs"),
    ANTI_MOB_MOVEMENT(new AntiMobMovement(SaberCore.getInstance()), "Disables Movement of Certain Entities"),
    ANTI_SPAWNER_MINE(new AntiSpawnerMine(SaberCore.getInstance()), "Disables Players Mining Spawners In a Configurable Radius Whilst Players Are Near"),
    ANTI_WATER_BLAZE(new AntiWaterBlaze(SaberCore.getInstance()), "Disable Blazes From Taking Water Damage"),
    ANTI_WILDERNESS_SPAWNER(new AntiWildernessSpawner(SaberCore.getInstance()), "Disable Spawners From Spawning In Wilderness"),
    AUTO_ENCHANTING_LAPIS(new AutoEnchantingLapis(SaberCore.getInstance()), "Automatically Inserts Lapis Into Enchantment Tables"),
    AUTO_RESPAWN(new AutoRespawn(SaberCore.getInstance()), "Automatically Respawn"),
    BORDER_PATCHES(new BorderPatches(SaberCore.getInstance()), "Overall Border Patches For World Border"),
    CHAT_FILTER(new ChatFilter(SaberCore.getInstance()), "Enables a dynamic word filtering system via /chatfilter."),
    DISABLED_COMMANDS(new DisabledCommands(SaberCore.getInstance()), "Denies Users To Specific Commands"),
    INSTA_SPONGE_BREAK(new InstaSpongeBreak(SaberCore.getInstance()), "Instantly Breaks Sponges When Swung At"),
    SOAK_SPONGES(new SoakSponges(SaberCore.getInstance()), "Soak Water/Lava Up With Sponges In a Configurable Radius"),
    SPAWNER_SPONGE(new SpawnerSponge(SaberCore.getInstance()), "Soak Water/Lava Up With Spawners In a Configurable Radius");

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
