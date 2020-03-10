package me.driftay.core.modules;

import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiCreeperGlitch extends SaberPlugin {
    public AntiCreeperGlitch(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiCreeperGlitchListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiCreeperGlitchListener extends SaberPluginListener<AntiCreeperGlitch> {

        public AntiCreeperGlitchListener(AntiCreeperGlitch patch) {
            super(patch);
        }

        @EventHandler
        public void onCreeperGlitch(EntityDamageEvent e) {
            if (!e.getEntity().getType().equals(EntityType.CREEPER)) return;
            if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) || e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                e.getEntity().remove();
            }
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player p = event.getPlayer();
            ItemStack i = p.getItemInHand();
            Block b = event.getClickedBlock();
            if (i.getType() != XMaterial.CREEPER_SPAWN_EGG.parseMaterial()) return;
            if (b == null) return;
            if (b.getType() != XMaterial.SPAWNER.parseMaterial()) return;
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            SpawnEgg egg = (SpawnEgg) i.getData();
            p.getWorld().spawnEntity(b.getLocation(), egg.getSpawnedType());
            if (i.getAmount() >= 2) {
                i.setAmount(i.getAmount() - 1);
                p.setItemInHand(i);
            } else {
                p.setItemInHand(new ItemStack(Material.AIR));
            }
            event.setCancelled(true);
        }
    }
}
