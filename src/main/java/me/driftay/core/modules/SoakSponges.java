package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class SoakSponges extends SaberPlugin {
    public SoakSponges(Plugin p) {
        super(p);
    }

    private static boolean isSponge(Material material) {
        return material.equals(Material.SPONGE);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new SoakSpongesListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class SoakSpongesListener extends SaberPluginListener<SoakSponges> {

        public SoakSpongesListener(SoakSponges patch) {
            super(patch);
        }

        @EventHandler
        public void on(BlockFromToEvent e) {
            if (e.isCancelled()) return;

            Block block = e.getBlock();
            World world = block.getWorld();
            int radius = SaberCore.getInstance().getConfig().getInt("settings.sponge-radius");
            int blockX = block.getX();
            int blockY = block.getY();
            int blockZ = block.getZ();
            for (int fromX = -(radius + 1); fromX <= radius + 1; ++fromX) {
                for (int fromY = -(radius + 1); fromY <= radius + 1; ++fromY) {
                    for (int fromZ = -(radius + 1); fromZ <= radius + 1; ++fromZ) {
                        Block b = world.getBlockAt(blockX + fromX, blockY + fromY, blockZ + fromZ);
                        if (b.getType().equals(XMaterial.SPONGE.parseMaterial())) {
                            if (e.isCancelled()) {
                                return;
                            }
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent e) {
            if (e.isCancelled()) return;

            Player player = e.getPlayer();
            if (e.getBlock().getType() == XMaterial.SPONGE.parseMaterial()) {
                for (int radius = SaberCore.getInstance().getConfig().getInt("settings.sponge-radius"), x = -radius; x <= radius; ++x) {
                    for (int y = -radius; y <= radius; ++y) {
                        for (int z = -radius; z <= radius; ++z) {
                            Location locIntorno = new Location(player.getWorld(), e.getBlock().getX() + x, e.getBlock().getY() + y, e.getBlock().getZ() + z);
                            if (locIntorno.getBlock().getType() == XMaterial.LAVA.parseMaterial() || locIntorno.getBlock().getType() == XMaterial.WATER.parseMaterial()) {
                                locIntorno.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onWaterBucket(PlayerInteractEvent playerInteractEvent) {
            if (!playerInteractEvent.isCancelled() && playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                int i = SaberCore.getInstance().getConfig().getInt("settings.sponge-radius");
                Block clickedBlock = playerInteractEvent.getClickedBlock();
                World world = clickedBlock.getWorld();
                if (playerInteractEvent.getPlayer().getItemInHand().getType().equals(XMaterial.WATER_BUCKET.parseMaterial())) {
                    int x = clickedBlock.getX();
                    int y = clickedBlock.getY();
                    int z = clickedBlock.getZ();
                    for (int i2 = -i; i2 <= i; i2++) {
                        for (int i3 = -i; i3 <= i; i3++) {
                            for (int i4 = -i; i4 <= i; i4++) {
                                if (isSponge(world.getBlockAt(x + i2, y + i3, z + i4).getType())) {
                                    playerInteractEvent.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
