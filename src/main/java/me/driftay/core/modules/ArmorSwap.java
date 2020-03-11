package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/11/2020
 */
public class ArmorSwap extends SaberPlugin {
    public ArmorSwap(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new ArmorSwapListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class ArmorSwapListener extends SaberPluginListener<ArmorSwap> {

        public ArmorSwapListener(ArmorSwap patch) {
            super(patch);
        }

        public boolean isArmor(Material m) {
            String n = m.toString().toLowerCase();
            return m != null && m != Material.AIR && (n.contains("helmet") || n.contains("chestplate") || n.contains("leggings") || n.contains("boots"));
        }

        public void equipArmor(Player player, ItemStack is) {
            if (is == null || is.getType() == Material.AIR) {
                return;
            }
            String n = is.getType().toString().toLowerCase();
            PlayerInventory inv = player.getInventory();
            if (n.contains("helmet")) {
                ItemStack old = inv.getHelmet();
                inv.setHelmet(is);
                inv.removeItem(is);
                if (old != null && old.getType() != Material.AIR) inv.setItemInHand(old);
            } else if (n.contains("chestplate")) {
                ItemStack old = inv.getChestplate();
                inv.setChestplate(is);
                inv.removeItem(is);
                if (old != null && old.getType() != Material.AIR) inv.setItemInHand(old);
            } else if (n.contains("leggings")) {
                ItemStack old = inv.getLeggings();
                inv.setLeggings(is);
                inv.removeItem(is);
                if (old != null && old.getType() != Material.AIR) inv.setItemInHand(old);
            } else if (n.contains("boots")) {
                ItemStack old = inv.getBoots();
                inv.setBoots(is);
                inv.removeItem(is);
                if (old != null && old.getType() != Material.AIR) inv.setItemInHand(old);
            }
            player.updateInventory();
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerInteract(PlayerInteractEvent e) {
            if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            if (e.getPlayer().getItemInHand() == null || !this.isArmor(e.getPlayer().getItemInHand().getType())) return;

            if (e.hasBlock() && (e.getClickedBlock().getState() instanceof Chest || e.getClickedBlock().getState() instanceof Dispenser || e.getClickedBlock().getState() instanceof Dropper || e.getClickedBlock().getState() instanceof Furnace || e.getClickedBlock().getState() instanceof Hopper || e.getClickedBlock().getType() == Material.GOLD_BLOCK || e.getClickedBlock().getType() == Material.IRON_BLOCK)) {
                return;
            }

            Player p = e.getPlayer();

            if (p.hasMetadata("lastArmorSwap")){
                long dif = System.currentTimeMillis() - p.getMetadata("lastArmorSwap").get(0).asLong();
                if(dif < SaberCore.getInstance().getConfig().getLong("settings.armor-swap.cooldown"))
                return;
            }

            if (e.isCancelled() && p.hasMetadata("noArmorSwap")) return;

            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
            p.setMetadata("lastArmorSwap", new FixedMetadataValue(SaberCore.getInstance(), System.currentTimeMillis()));
            this.equipArmor(p, e.getItem());
        }
    }
}
