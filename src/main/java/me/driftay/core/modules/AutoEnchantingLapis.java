package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AutoEnchantingLapis extends SaberPlugin {

    public static ArrayList<EnchantingInventory> inventories = new ArrayList<>();

    public AutoEnchantingLapis(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AutoEnchantingLapisListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
        for (EnchantingInventory ei : inventories) {
            ei.setItem(1, null);
        }
        inventories = null;
    }

    static class AutoEnchantingLapisListener extends SaberPluginListener<AutoEnchantingLapis> {

        public AutoEnchantingLapisListener(AutoEnchantingLapis patch) {
            super(patch);
        }

        @EventHandler
        public void openInventoryEvent(InventoryOpenEvent e) {
            if (e.getInventory() instanceof EnchantingInventory) {
                e.getInventory().setItem(1, XMaterial.LAPIS_LAZULI.parseItem());
                inventories.add((EnchantingInventory) e.getInventory());
            }
        }

        @EventHandler
        public void closeInventoryEvent(InventoryCloseEvent e) {
            if (e.getInventory() instanceof EnchantingInventory && inventories.contains(e.getInventory())) {
                e.getInventory().setItem(1, null);
                inventories.remove(e.getInventory());
            }
        }

        @EventHandler
        public void inventoryClickEvent(InventoryClickEvent e) {
            if (e.getClickedInventory() instanceof EnchantingInventory && inventories.contains(e.getInventory()) && e.getSlot() == 1) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void enchantItemEvent(EnchantItemEvent e) {
            if (inventories.contains(e.getInventory())) {
                e.getInventory().setItem(1, XMaterial.LAPIS_LAZULI.parseItem());
            }
        }
    }
}
