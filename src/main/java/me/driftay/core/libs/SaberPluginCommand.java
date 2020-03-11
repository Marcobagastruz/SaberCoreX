package me.driftay.core.libs;

import me.driftay.core.plugins.SaberPlugin;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public abstract class SaberPluginCommand<T extends SaberPlugin> extends AbstractCommand {

    protected T patch;

    public SaberPluginCommand(T patch, String command) {
        super(command);
        this.patch = patch;
    }

    public SaberPluginCommand(T patch, String command, String usage, String description) {
        super(command, usage, description);
        this.patch = patch;
    }

    public SaberPluginCommand(T patch, String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
        this.patch = patch;
    }
}
