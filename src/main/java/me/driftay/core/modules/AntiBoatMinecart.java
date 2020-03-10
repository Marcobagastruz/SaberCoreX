package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiBoatMinecart extends SaberPlugin {
    public AntiBoatMinecart(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiBoatMinecartListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiBoatMinecartListener extends SaberPluginListener<AntiBoatMinecart> {

        public AntiBoatMinecartListener(AntiBoatMinecart patch) {
            super(patch);
        }

        @EventHandler
        public void onBoatPlace(PlayerInteractEvent event) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType().name().contains("BOAT")) {
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType().name().contains("MINECART")) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onDispenseItem(BlockDispenseEvent event) {
            if (event.getItem().getType().name().contains("BOAT")) {
                event.setCancelled(true);
            } else if (event.getItem().getType().name().contains("MINECART")) {
                event.setCancelled(true);
            }
        }
    }
}
