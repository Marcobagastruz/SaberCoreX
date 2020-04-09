package me.driftay.core.modules;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiSpawnerMine extends SaberPlugin {
    public AntiSpawnerMine(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiSpawnerMineListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiSpawnerMineListener extends SaberPluginListener<AntiSpawnerMine> {

        public AntiSpawnerMineListener(AntiSpawnerMine patch) {
            super(patch);
        }

        @EventHandler
        public void onSpawnerMine(BlockBreakEvent e) {
            if (!e.getBlock().getType().equals(XMaterial.SPAWNER.parseMaterial())) return;

            if (nearbyEnemies(e.getPlayer(), SaberCore.getInstance().getConfig().getDouble("settings.anti-spawner-mine-radius"), null) && !e.getPlayer().hasPermission("sabercore.spawnermine,bypass"))
                e.setCancelled(true);

            if (e.isCancelled())
                e.getPlayer().sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("anti-spawner-mine-players-near")));

        }

        public boolean nearbyEnemies(Player player, double d, String str) {
            List<Entity> nearbyEntities = player.getNearbyEntities(d, d, d);
            FPlayer byPlayer = FPlayers.getInstance().getByPlayer(player);
            for (Entity entity : nearbyEntities) {
                if ((entity instanceof Player) && (str == null || entity.hasPermission(str))) {
                    FPlayer byPlayer2 = FPlayers.getInstance().getByPlayer((Player) entity);
                    if (!(byPlayer2.getFactionId().equals(byPlayer.getFactionId()) || byPlayer2.getFaction().getRelationTo(byPlayer.getFaction()).isTruce() || byPlayer2.getFaction().getRelationTo(byPlayer.getFaction()).isAlly())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
