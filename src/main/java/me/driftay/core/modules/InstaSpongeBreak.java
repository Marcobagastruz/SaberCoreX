package me.driftay.core.modules;

import com.massivecraft.factions.*;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class InstaSpongeBreak extends SaberPlugin {

    public InstaSpongeBreak(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new InstaSpongeBreakListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class InstaSpongeBreakListener extends SaberPluginListener<InstaSpongeBreak> {

        public InstaSpongeBreakListener(InstaSpongeBreak patch) {
            super(patch);
        }

        @EventHandler
        public void onClick(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
                return;
            }
            Block block = event.getClickedBlock();
            if (block.getType() == Material.SPONGE) {
                FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
                Faction faction = fPlayer.getFaction();
                Faction location = Board.getInstance().getFactionAt(new FLocation(block.getLocation()));
                if (faction.getId().equals(location.getId()) || (location.isNone())) {
                    block.breakNaturally();
                    event.setCancelled(true);
                }
            }
        }
    }
}
