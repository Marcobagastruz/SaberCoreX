package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/15/2020
 */
public class NoCursorDrop extends SaberPlugin {
    public NoCursorDrop(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new NoCursorDropListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class NoCursorDropListener extends SaberPluginListener<NoCursorDrop> {

        public NoCursorDropListener(NoCursorDrop patch) {
            super(patch);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onInventoryClose(InventoryCloseEvent e) {
            Player player = (Player)e.getPlayer();
            if (player.getItemOnCursor() != null && player.getInventory().firstEmpty() != -1 && !player.isDead() && player.getHealth() > 0.0) {
                ItemStack i = player.getItemOnCursor().clone();
                player.setItemOnCursor(null);
                player.getInventory().addItem(i);
                player.updateInventory();
            }
        }
    }
}
