package me.driftay.core.utils;

import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class LocationUtils {

    public static String printPretty(Location location, ChatColor color, boolean bold) {
        String xyzBefore = "%sx%s %sy%s %sz%s";
        String boldText = bold ? ChatColor.translateAlternateColorCodes('&', "&l") : "";
        return String.format(xyzBefore, color, color + boldText + location.getBlockX(), color, color + boldText + location.getBlockY(), color, color + boldText + location.getBlockZ());
    }

    public static Location getLocationFromString(String s, boolean loadWorld) {
        if (!s.contains(":")) return null;

        String[] args = s.split(":");
        if (args.length == 6) {
            String worldName = args[0];
            World world = Bukkit.getWorld(worldName);
            if (world == null && loadWorld)
                world = Bukkit.createWorld(new WorldCreator(worldName).generateStructures(false));
            return new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
        }
        if (args.length == 4) {
            String worldName = args[0];
            World world = Bukkit.getWorld(worldName);
            if (world == null && loadWorld)
                world = Bukkit.createWorld(new WorldCreator(worldName).generateStructures(false));
            return new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        }
        return null;
    }

    public static String getStringFromLocation(Location l) {
        return l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getYaw() + ":" + l.getPitch();
    }

    public static List<String> getStringsFromLocations(List<Location> locs) {
        List<String> strings = new ArrayList<>();
        for (Location l : locs) strings.add(getStringFromLocation(l));
        return strings;
    }

    public static List<Location> getLocationsFromStringList(List<String> strings, boolean load) {
        List<Location> locs = new ArrayList<>();
        for (String s : strings) {
            locs.add(getLocationFromString(s, load));
        }
        return locs;
    }

    public static Location getNearbyLocation(Location l, int minDistance, int maxDistance) {
        Random rand = new Random();
        Location rand_loc = l.clone();
        rand_loc.add((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(maxDistance) + minDistance), 0.0D, (rand.nextBoolean() ? 1 : -1) * (rand.nextInt(maxDistance) + minDistance));
        rand_loc.add(0.5D, 0.0D, 0.5D);
        Block b = rand_loc.getWorld().getHighestBlockAt(rand_loc).getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();
        boolean badMaterial = b.isLiquid() || b.getType() == XMaterial.WATER.parseMaterial() || b.getType() == XMaterial.SNOW.parseMaterial();
        if (badMaterial) {
            return getNearbyLocation(l, minDistance, maxDistance);
        } else {
            while ((rand_loc.getBlock().getType() != Material.AIR || rand_loc.getBlock().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) && rand_loc.getY() < 255.0D) {
                rand_loc = rand_loc.add(0.0D, 1.0D, 0.0D);
            }

            return rand_loc;
        }
    }
}
