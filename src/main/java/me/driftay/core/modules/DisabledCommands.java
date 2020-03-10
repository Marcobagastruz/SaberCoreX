package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class DisabledCommands extends SaberPlugin {
    public DisabledCommands(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new DisabledCommandsListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class DisabledCommandsListener extends SaberPluginListener<DisabledCommands> {

        public DisabledCommandsListener(DisabledCommands patch) {
            super(patch);
        }

        @EventHandler
        public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
            Player p = e.getPlayer();
            String message = e.getMessage();
            List<String> list = SaberCore.getInstance().getConfig().getStringList("settings.disabled-commands");

            for (String s : list) {
                if (message.equalsIgnoreCase(s) && !p.hasPermission("sabercore.disabledcommands.bypass")) {
                    e.setCancelled(true);
                    p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("disabled-command-prompt").replace("{command}", message.toLowerCase())));
                }
            }
        }
    }
}
