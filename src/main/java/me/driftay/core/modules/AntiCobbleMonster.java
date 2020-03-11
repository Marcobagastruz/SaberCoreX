package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiCobbleMonster extends SaberPlugin {
    public AntiCobbleMonster(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiCobbleMonsterListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiCobbleMonsterListener extends SaberPluginListener<AntiCobbleMonster> {

        private final BlockFace[] faces = {BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        public AntiCobbleMonsterListener(AntiCobbleMonster patch) {
            super(patch);
        }

        @EventHandler
        public void onFromTo(BlockFromToEvent event) {
            int id = event.getBlock().getType().getId();
            Block b = event.getToBlock();
            if (generatesCobble(id, b)) event.setCancelled(true);
        }

        public boolean generatesCobble(int id, Block b) {
            int mirrorID1 = (id == 8) || (id == 9) ? 10 : 8;
            int mirrorID2 = (id == 8) || (id == 9) ? 11 : 9;
            for (BlockFace face : this.faces) {
                Block r = b.getRelative(face, 1);
                if ((r.getType().getId() == mirrorID1) || (r.getType().getId() == mirrorID2)) return true;
            }
            return false;
        }
    }
}
