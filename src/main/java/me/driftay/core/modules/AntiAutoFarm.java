package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiAutoFarm extends SaberPlugin {
    public AntiAutoFarm(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiAutoFarmListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiAutoFarmListener extends SaberPluginListener<AntiAutoFarm> {

        public AntiAutoFarmListener(AntiAutoFarm patch) {
            super(patch);
        }

        @EventHandler
        public void onRetract(BlockPistonExtendEvent event) {
            Block to = event.getBlock().getRelative(event.getDirection());
            Block nextBlock = to.getRelative(event.getDirection());
            if (nextBlock.getType() == XMaterial.SUGAR_CANE.parseMaterial()
                    || nextBlock.getType() == XMaterial.MELON.parseMaterial()
                    || nextBlock.getType() == XMaterial.MELON_STEM.parseMaterial() || nextBlock.getType() == XMaterial.GLISTERING_MELON_SLICE.parseMaterial()) {
                event.setCancelled(true);
            }
            if (to.getType() == XMaterial.SUGAR_CANE.parseMaterial()
                    || (to.getType() == XMaterial.MELON.parseMaterial())
                    || to.getType() == XMaterial.MELON_STEM.parseMaterial() || to.getType() == XMaterial.GLISTERING_MELON_SLICE.parseMaterial()) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onFluxPatch(BlockPistonExtendEvent event) {
            Block to = event.getBlock().getRelative(event.getDirection());
            Block nextBlock = to.getRelative(event.getDirection());
            if ((to.getType().toString().endsWith("_GATE") || to.getType().toString().endsWith("_FENCE"))
                    || (nextBlock.getType().toString().endsWith("_GATE")
                    || nextBlock.getType().toString().endsWith("_FENCE"))) {
                event.setCancelled(true);
            }
        }
    }
}
