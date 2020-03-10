package me.driftay.core;

import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.plugins.SaberPlugins;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class SaberCore extends JavaPlugin {

    public static SaberCore instance;
    public boolean shutting_down = false;
    private HashMap<String, SaberPlugin> loaded_modules = new HashMap<>();

    public static SaberCore getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        //Register Modules Last
        Bukkit.getScheduler().runTaskLater(this, this::registerPatches, 20L);
    }

    public void onDisable() {
        this.shutting_down = true;
        this.saveConfig();
        this.unregisterPatches();
    }

    public boolean registerPatch(SaberPlugin ap) {
        SaberPlugins aps = SaberPlugins.reflectSaberPlugin(ap);
        if (aps != null && !this.loaded_modules.containsKey(aps.getConfigName())) {
            ap.inject();
            this.loaded_modules.put(aps.getConfigName(), ap);
            return true;
        } else {
            return false;
        }
    }

    public boolean unregisterPatch(String config_path) {
        SaberPlugin ap = this.loaded_modules.getOrDefault(config_path, null);
        if (ap == null) {
            return false;
        } else {
            ap.kill();
            this.loaded_modules.remove(config_path);
            return true;
        }
    }

    public void registerPatches() {
        for (SaberPlugins aps : SaberPlugins.values()) {
            if (!this.getConfig().contains("modules." + aps.getConfigName())) {
                this.getConfig().set("modules." + aps.getConfigName(), false);
            } else if (this.getConfig().getBoolean("modules." + aps.getConfigName())) {
                this.registerPatch(aps.getSaberPlugin());
            }
        }
        this.saveConfig();
    }

    public void unregisterPatches() {
        for (SaberPlugin ap : this.loaded_modules.values()) ap.kill();
        this.loaded_modules.clear();
    }


}
