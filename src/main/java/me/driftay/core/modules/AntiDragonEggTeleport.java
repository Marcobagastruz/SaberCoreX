package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiDragonEggTeleport extends SaberPlugin {
    public AntiDragonEggTeleport(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiDragonEggTeleportListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiDragonEggTeleportListener extends SaberPluginListener<AntiDragonEggTeleport> {

        public AntiDragonEggTeleportListener(AntiDragonEggTeleport patch) {
            super(patch);
        }

        @EventHandler
        public void onDeggClick(PlayerInteractEvent e) {
            Block b = e.getClickedBlock();

            if (b == null) return;

            if (b.getType() != Material.DRAGON_EGG) return;

            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            e.setCancelled(true);
        }

        @EventHandler
        public void onBlockChange(BlockFromToEvent event) {
            if (event.getBlock().getType() == Material.DRAGON_EGG) event.setCancelled(true);
        }
    }
}
