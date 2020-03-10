package me.driftay.core.modules;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.plugin.Plugin;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class AntiWildernessSpawner extends SaberPlugin {
    public AntiWildernessSpawner(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new AntiWildernessSpawnerListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
    }

    static class AntiWildernessSpawnerListener extends SaberPluginListener<AntiWildernessSpawner> {

        public AntiWildernessSpawnerListener(AntiWildernessSpawner patch) {
            super(patch);
        }

        @EventHandler
        public void onSpawner(SpawnerSpawnEvent e) {
            FLocation floc = new FLocation(e.getSpawner().getLocation());
            if (floc == null) return;
            Faction faction = Board.getInstance().getFactionAt(floc);
            if (faction == null) return;
            if (faction.isWilderness()) e.setCancelled(true);
        }
    }
}
