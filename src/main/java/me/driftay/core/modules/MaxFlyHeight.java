package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/11/2020
 */
public class MaxFlyHeight extends SaberPlugin {
    public MaxFlyHeight(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerTask(new MaxFlyHeightTask().runTaskTimer(SaberCore.getInstance(), 2L, 2L), MaxFlyHeightTask.class);
    }

    @Override
    public void disable() {
    }
}

