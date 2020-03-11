package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiItemStorage extends SaberPlugin {
    public static List<String> itemList = SaberCore.getInstance().getConfig().getStringList("settings.denied-storage");

    public AntiItemStorage(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiItemStorageListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiItemStorageListener extends SaberPluginListener<AntiItemStorage> {

        public AntiItemStorageListener(AntiItemStorage patch) {
            super(patch);
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e) {
            Player player = (Player) e.getWhoClicked();

            if (e.isCancelled()) return;

            Inventory clicked = e.getClickedInventory();

            if (e.getClick().isShiftClick()) {
                if (clicked == e.getWhoClicked().getInventory()) {
                    ItemStack clickedOn = e.getCurrentItem();
                    if (clickedOn != null && itemList.contains(XMaterial.matchXMaterial(clickedOn.getType().toString()))) {
                        player.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("cannot-store-item").replace("{item}", clickedOn.getType().toString())));
                        e.setCancelled(true);
                    }
                }
            }

            if (clicked != e.getWhoClicked().getInventory()) {
                ItemStack onCursor = e.getCursor();
                if (onCursor != null && itemList.contains(XMaterial.matchXMaterial(onCursor.getType().toString()))) {
                    player.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("cannot-store-item").replace("{item}", onCursor.getType().toString())));
                    e.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void onInventoryDrag(InventoryDragEvent e) {
            Player p = (Player) e.getWhoClicked();

            if (e.isCancelled()) return;

            ItemStack dragged = e.getOldCursor();
            if (itemList.contains(XMaterial.matchXMaterial(dragged.getType().toString()))) {
                int inventorySize = e.getInventory().getSize();
                for (int i : e.getRawSlots()) {
                    if (i < inventorySize) {
                        p.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("cannot-store-item").replace("{item}", dragged.getType().toString())));
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }

        @EventHandler
        public void onHopperMoveEvent(InventoryMoveItemEvent e) {
            if (e.isCancelled()) return;
            if (itemList.contains(XMaterial.matchXMaterial(e.getItem().getType().toString()))) {
                e.setCancelled(true);
            }
        }
    }
}
