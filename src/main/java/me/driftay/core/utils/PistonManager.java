package me.driftay.core.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class PistonManager extends HashMap<String, HashSet<Location>> {
    private static final long serialVersionUID = -6754405085225360026L;

    public void addPiston(Block b) {
        Chunk c = b.getChunk();
        int cx = c.getX();
        int cz = c.getZ();
        HashSet<Location> hloc;
        if (this.containsKey(cx + "," + cz)) {
            hloc = this.get(cx + "," + cz);
            hloc.add(b.getLocation());
        } else {
            hloc = new HashSet<>(Collections.singletonList(b.getLocation()));
        }
        this.put(cx + "," + cz, hloc);

    }

    public void removePiston(Block b) {
        Chunk c = b.getChunk();
        int cx = c.getX();
        int cz = c.getZ();
        if (this.containsKey(cx + "," + cz)) {
            HashSet<Location> hloc = this.get(cx + "," + cz);
            hloc.remove(b.getLocation());
            this.put(cx + "," + cz, hloc);
        }
    }

    public boolean containsPiston(Block b) {
        Chunk c = b.getChunk();
        int cx = c.getX();
        int cz = c.getZ();
        return this.containsKey(cx + "," + cz) && this.get(cx + "," + cz).contains(b.getLocation());
    }

    public int getPistonCount(Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        return this.containsKey(cx + "," + cz) ? this.get(cx + "," + cz).size() : 0;
    }

    public boolean isAllowedPiston(Block b) {
        return this.containsPiston(b);
    }
}

