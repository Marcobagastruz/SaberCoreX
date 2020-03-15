package me.driftay.core.modules.shockwaves;

import com.massivecraft.factions.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import itemnbtapi.NBTItem;
import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.ItemUtils;
import me.driftay.core.utils.struct.XMaterial;
import net.coreprotect.CoreProtect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;

import static me.driftay.core.utils.StringUtils.translate;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/15/2020
 */
public class Shockwave extends SaberPlugin {
    public Shockwave(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerListener(new ShockwaveListener(this));
        this.registerCommand(new CommandShockwave(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterListeners();
        this.unregisterCommands();
    }

    static class ShockwaveListener extends SaberPluginListener<Shockwave> {

        public ShockwaveListener(Shockwave patch) {
            super(patch);
        }

        @SuppressWarnings("deprecation")
        @EventHandler
        public void onShockwave(BlockBreakEvent e) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_PICKAXE) ||
                    e.getPlayer().getItemInHand().getType().equals(XMaterial.DIAMOND_SHOVEL.parseMaterial()) ||
                    e.getPlayer().getItemInHand().getType() == Material.DIAMOND_HOE) {
                NBTItem nbti = new NBTItem(e.getPlayer().getItemInHand());
                if (nbti.hasKey("Shockwave")) {
                    if (nbti.getBoolean("Shockwave")) {
                        int radius = nbti.getInteger("Radius");
                        shockwaveBreak(e, radius);
                    }
                }
            }
        }

        private WorldGuardPlugin getWorldGuard() {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

            // WorldGuard may not be loaded
            if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
                return null; // Maybe you want throw an exception instead
            }

            return (WorldGuardPlugin) plugin;
        }

        @SuppressWarnings("deprecation")
        public void shockwaveBreak(BlockBreakEvent e, int radius) {
            Player p = e.getPlayer();
            Faction UUIDfac;
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
            UUIDfac = fplayer.getFaction();

            boolean spade = false;
            if (e.getPlayer().getItemInHand().getType().equals(XMaterial.DIAMOND_SHOVEL.parseMaterial())) {
                spade = true;
            }
            int goesInto = radius / 2;
            radius = radius - (goesInto + 1);
            for (double x = e.getBlock().getLocation().getX() - radius; x <= e.getBlock().getLocation().getX() + radius; x++) {
                for (double y = e.getBlock().getLocation().getY() - radius; y <= e.getBlock().getLocation().getY() + radius; y++) {
                    for (double z = e.getBlock().getLocation().getZ() - radius; z <= e.getBlock().getLocation().getZ() + radius; z++) {
                        Location loc = new Location(e.getBlock().getWorld(), x, y, z);
                        if (loc.getBlock().getType() == Material.BEDROCK || loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == XMaterial.SPAWNER.parseMaterial()) {
                            continue;
                        }
                        if (!Objects.requireNonNull(getWorldGuard()).canBuild(e.getPlayer(), loc)) {
                            continue;
                        }
                        if (spade) {
                            if (loc.getBlock().getType() != XMaterial.DIRT.parseMaterial()
                                    && loc.getBlock().getType() != XMaterial.GRAVEL.parseMaterial()
                                    && loc.getBlock().getType() != XMaterial.SAND.parseMaterial()
                                    && loc.getBlock().getType() != XMaterial.GRASS.parseMaterial()) {
                                continue;
                            }
                        }

                        FLocation floc = new FLocation(loc);
                        Faction fac = Board.getInstance().getFactionAt(floc);
                        if (fac.isWilderness() || fac.getId().equals(UUIDfac.getId())) {
                            p.getInventory().addItem(new ItemStack(loc.getBlock().getType()));
                            loc.getBlock().setType(Material.AIR);
                            if(Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
                                if (SaberCore.getInstance().getFileManager().getConfig().fetchBoolean("settings.shockwave.log-core-protect")) {
                                    CoreProtect.getInstance().getAPI().logRemoval(p.getName(), loc, loc.getBlock().getType(), loc.getBlock().getData());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static class CommandShockwave extends SaberPluginCommand<Shockwave> {

        public CommandShockwave(Shockwave patch) {
            super(patch, "shockwave");
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 4) {
                if (!sender.hasPermission("sabercore.shockwave.give")) {
                    sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                    return true;
                }
                if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("pickaxe") || args[1].equalsIgnoreCase("shovel") || args[1].equalsIgnoreCase("hoe")) {

                        if (Integer.parseInt(args[2]) % 2 == 0) {
                            sender.sendMessage(translate("&c&l[!] &7The radius need to be an odd number!"));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[3]) == null || !Bukkit.getPlayer(args[3]).isOnline()) {
                            sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.player-not-found")).replace("{player}", args[3]));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("shovel")) args[1] = "spade";
                        Material material = Material.getMaterial("DIAMOND_" + args[1].toUpperCase());

                        String name = translate(SaberCore.getInstance().getFileManager().getConfig().fetchString("settings.shockwave.item.display-name"));
                        name = name.replace("{radius}", args[2]);
                        List<String> lore = null;
                        if (args[1].equalsIgnoreCase("pickaxe")) {
                            name = name.replace("{itemtype}", "Pickaxe");
                            lore = translate(SaberCore.getInstance().getFileManager().getConfig().fetchStringList("settings.shockwave.item.lore"));
                        }
                        if (args[1].equalsIgnoreCase("spade")) {
                            name = name.replace("{itemtype}", "Shovel");
                            lore = translate(SaberCore.getInstance().getFileManager().getConfig().fetchStringList("settings.shockwave.item.lore"));
                        }
                        if (args[1].equalsIgnoreCase("hoe")) {
                            name = name.replace("{itemtype}", "Hoe");
                            lore = translate(SaberCore.getInstance().getFileManager().getConfig().fetchStringList("settings.shockwave.item.lore"));
                        }
                        if (lore != null) {
                            for (int i = 0; i <= lore.size() - 1; i++) {
                                lore.set(i, lore.get(i).replace("{radius}", args[2]));
                            }
                        }
                        ItemStack shockwaveItem = ItemUtils.createItem(material, 1, (short) 0, name, lore);
                        shockwaveItem.addUnsafeEnchantment(Enchantment.DIG_SPEED, SaberCore.getInstance().getFileManager().getConfig().fetchInt("settings.shockwave.efficiency-level"));
                        shockwaveItem.addUnsafeEnchantment(Enchantment.DURABILITY, SaberCore.getInstance().getFileManager().getConfig().fetchInt("settings.shockwave.unbreaking-level"));
                        sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("shockwave.received")));
                        NBTItem nbti = new NBTItem(shockwaveItem);
                        nbti.setBoolean("Shockwave", true);
                        nbti.setInteger("Radius", Integer.parseInt(args[2]));
                        Bukkit.getPlayer(args[3]).getInventory().addItem(nbti.getItem());
                        return true;


                    }
                }
            } else if (sender.hasPermission("sabercore.shockwave.give")) {
                sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("shockwave.command-usage")));
            }
            return true;
        }
    }


    }
