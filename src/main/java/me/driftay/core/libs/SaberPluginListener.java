package me.driftay.core.libs;

import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.event.Listener;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */

public abstract class SaberPluginListener<T extends SaberPlugin> implements Listener {
    protected T patch;

    public SaberPluginListener(T patch) {
        this.patch = patch;
    }
}
