package me.driftay.core.modules.chunkbuster;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.driftay.core.SaberCore;
import me.driftay.core.hooks.HookManager;
import me.driftay.core.hooks.impl.FactionHook;
import me.driftay.core.hooks.impl.WorldGuardHook;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.ItemCreation;
import me.driftay.core.utils.struct.XMaterial;
import net.coreprotect.CoreProtect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;

import static me.driftay.core.utils.StringUtils.translate;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/15/2020
 */
public class Chunkbusters extends SaberPlugin {
    public static HashMap<Chunk, Location> beingBusted = new HashMap<>();

    public static HashSet<Chunk> waterChunks = new HashSet<>();


    public Chunkbusters(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new ChunkbustersListener(this));
        this.registerCommand(new ChunkbustersListener.CommandChunkbusters(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
        this.unregisterCommands();
    }

    static class ChunkbustersListener extends SaberPluginListener<Chunkbusters> {

        public ChunkbustersListener(Chunkbusters patch) {
            super(patch);
        }


        @EventHandler
        public void onBusterPlace(BlockPlaceEvent e) {
            Player player = e.getPlayer();
            Block block = e.getBlock();

            if (e.getItemInHand().getType().equals(XMaterial.END_PORTAL_FRAME.parseMaterial())) {
                if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(translate(SaberCore.getInstance().getFileManager().getConfig().fetchString("settings.chunkbuster.item.displayname")))) {
                    Block b = e.getBlockPlaced();

                    if (HookManager.getPluginMap().get("WorldGuard") != null) {
                        WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                        if (!wgHook.canBuild(player, block)) {
                            e.getPlayer().sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("chunkbuster.cant-place")));
                            return;
                        }
                    }

                    FactionHook facHook = (FactionHook) HookManager.getPluginMap().get("Factions");
                    if (!facHook.canBuild(block, player)) {
                        e.setCancelled(true);
                        return;
                    }


                    if (beingBusted.containsKey(block.getLocation().getChunk())) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("chunkbuster.being-busted")));
                        return;
                    }

                    Entity[] entities = e.getBlock().getChunk().getEntities();
                    for (int i = 0; i <= entities.length - 1; i++) {
                        if (entities[i] instanceof HumanEntity) {
                            entities[i].sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("chunkbuster.use-message")));
                        }
                    }

                    World world = e.getBlock().getWorld();
                    int bx = b.getChunk().getX() << 4;
                    int bz = b.getChunk().getZ() << 4;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SaberCore.instance, () -> {
                        beingBusted.put(block.getLocation().getChunk(), block.getLocation());
                        waterChunks.add(b.getChunk());
                        for (int xx = bx; xx < bx + 16; xx++) {
                            for (int zz = bz; zz < bz + 16; zz++) {
                                for (int yy = e.getBlockPlaced().getY(); yy >= 0; yy--) {
                                    Block block1 = world.getBlockAt(xx, yy, zz);
                                    if (block1.getType().equals(Material.AIR) || block1.getType().equals(XMaterial.SPAWNER.parseMaterial()) || block1.getType().equals(XMaterial.CHEST.parseMaterial()) || block1.getType().equals(XMaterial.END_PORTAL_FRAME.parseMaterial()) || block1.getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial())) {
                                        continue;
                                    }
                                    if (!block1.getType().equals(Material.BEDROCK)) {
                                        if (Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
                                            CoreProtect.getInstance().getAPI().logRemoval(player.getName(), block1.getLocation(), block1.getType(), block1.getData());
                                        }
                                        block1.setType(XMaterial.GLASS.parseMaterial());
                                    }
                                }
                            }
                        }
                    }, 0);
                    if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                        if (SaberCore.getInstance().getFileManager().getConfig().fetchBoolean("settings.chunkbuster.hologram.enabled")) {
                            Hologram hologram = HologramsAPI.createHologram(SaberCore.instance, b.getLocation().add(0.5, 1.5, 0.5));
                            hologram.appendTextLine(translate(SaberCore.getInstance().getFileManager().getConfig().fetchString("settings.chunkbuster.hologram.format")));
                            Bukkit.getScheduler().runTaskTimer(SaberCore.instance, new Runnable() {
                                int timer = SaberCore.getInstance().getFileManager().getConfig().fetchInt("settings.chunkbuster.warmup");

                                @Override
                                public void run() {
                                    if (timer == 0) {
                                        hologram.delete();
                                        return;
                                    }
                                    if (hologram.size() == 2) {
                                        hologram.getLine(1).removeLine();
                                    }
                                    hologram.appendTextLine((ChatColor.DARK_RED + "" + timer));
                                    timer--;
                                }
                            }, 0L, 20L);
                        }
                    }
                    if (SaberCore.getInstance().getFileManager().getConfig().fetchBoolean("settings.chunkbuster.async-mode")) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SaberCore.instance, () -> {
                            int multiplier = 0;
                            for (int yy = e.getBlockPlaced().getY(); yy >= 0; yy--) {
                                multiplier++;
                                int dy = yy;
                                Bukkit.getScheduler().scheduleSyncDelayedTask(SaberCore.instance, () -> {
                                    for (int zz = bz; zz < bz + 16; zz++) {
                                        for (int xx = bx; xx < bx + 16; xx++) {
                                            Block block12 = world.getBlockAt(xx, dy, zz);
                                            if (block12.getType().equals(Material.AIR) || block12.getType().equals(XMaterial.SPAWNER.parseMaterial()) || block12.getType().equals(XMaterial.CHEST.parseMaterial()) || block12.getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial())) {
                                                continue;
                                            }
                                            if (!block12.getType().equals(Material.BEDROCK)) {
                                                if (Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
                                                    CoreProtect.getInstance().getAPI().logRemoval(player.getName(), block12.getLocation(), block12.getType(), block12.getData());
                                                }
                                                block12.setType(Material.AIR);
                                            }
                                        }
                                        beingBusted.remove(e.getBlock().getChunk());
                                    }
                                }, 20L * multiplier);

                            }
                        }, SaberCore.getInstance().getFileManager().getConfig().fetchInt("settings.chunkbuster.warmup") * 20L);
                    } else {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SaberCore.instance, () -> {
                            for (int xx = bx; xx < bx + 16; xx++) {
                                for (int zz = bz; zz < bz + 16; zz++) {
                                    for (int yy = 0; yy < 255; yy++) {
                                        Block block13 = world.getBlockAt(xx, yy, zz);
                                        if (block13.getType().equals(Material.AIR) || block13.getType().equals(XMaterial.SPAWNER.parseMaterial()) || block13.getType().equals(XMaterial.CHEST.parseMaterial()) || block13.getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial())) {
                                            continue;
                                        }
                                        if (!block13.getType().equals(Material.BEDROCK)) {
                                            if (Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
                                                CoreProtect.getInstance().getAPI().logRemoval(player.getName(), block13.getLocation(), block13.getType(), block13.getData());
                                            }
                                            block13.setType(Material.AIR);
                                        }
                                    }
                                    beingBusted.remove(e.getBlock().getChunk());
                                }
                            }
                        }, SaberCore.getInstance().getFileManager().getConfig().fetchInt("settings.chunkbuster.warmup") * 20L);
                    }
                }
            }
        }

        static class CommandChunkbusters extends SaberPluginCommand<Chunkbusters> {

            public CommandChunkbusters(Chunkbusters patch) {
                super(patch, "chunkbuster");
            }

            @Override
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("give")) {
                        if (!sender.hasPermission("sabercore.chunkbusters.give")) {
                            sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) == null || !Bukkit.getPlayer(args[1]).isOnline()) {
                            sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.player-not-found")).replace("{player}", args[1]));
                            return true;
                        }
                        int amount = Integer.parseInt(args[2]);
                        if (amount >= 1) {
                            if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
                                Bukkit.getServer().getPlayer(args[1]).getInventory().addItem(ItemCreation.giveChunkBuster(amount));
                                Bukkit.getServer().getPlayer(args[1]).sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("chunkbuster.received")));
                                return true;
                            }
                        }
                    }
                } else if (sender.hasPermission("sabercore.chunkbusters.give")) {
                    sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("chunkbuster.command-usage")));
                }
                return false;
            }
        }


        public HashSet<Chunk> getWaterChunks() {
            return waterChunks;
        }

        @EventHandler
        public void onWaterFlow(BlockFromToEvent e) {
            if (Bukkit.getVersion().contains("1.13")) {
                if (e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.LAVA)) {
                    if (!getWaterChunks().contains(e.getBlock().getChunk()) && getWaterChunks().contains(e.getToBlock().getChunk())) {
                        e.setCancelled(true);
                    }
                }
            } else {
                if (e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_WATER"))
                        || e.getBlock().getType().equals(Material.LAVA) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_LAVA"))) {
                    if (!getWaterChunks().contains(e.getBlock().getChunk()) && getWaterChunks().contains(e.getToBlock().getChunk())) {
                        e.setCancelled(true);
                    }
                }
            }
        }


    }
}
