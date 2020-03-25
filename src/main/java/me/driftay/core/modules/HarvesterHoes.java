package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.libs.SaberPluginListener;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.EconomyUtils;
import me.driftay.core.utils.ItemCreation;
import me.driftay.core.utils.StringUtils;
import me.driftay.core.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static me.driftay.core.utils.StringUtils.translate;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/25/2020
 */
public class HarvesterHoes extends SaberPlugin {
    public HarvesterHoes(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerCommand(new HarvesterHoesCommand(this));
        this.registerListener(new HarvesterHoesListener(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterCommands();
        this.unregisterListeners();
    }


    static class HarvesterHoesCommand extends SaberPluginCommand<HarvesterHoes> {


        public HarvesterHoesCommand(HarvesterHoes patch) {
            super(patch, "harvesterhoe");
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (!sender.hasPermission("sabercore.harvester.give")) {
                        sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[1]) == null || !Bukkit.getPlayer(args[1]).isOnline()) {
                        sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.player-not-found")).replace("{player}", args[1]));
                        return true;
                    }
                    int amount = Integer.parseInt(args[2]);
                    if (amount >= 1) {
                        if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
                            Bukkit.getServer().getPlayer(args[1]).getInventory().addItem(ItemCreation.giveHarvesterHoe(amount));
                            Bukkit.getServer().getPlayer(args[1]).sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("harvester-hoe.received")));
                            return true;
                        }
                    }
                }
            } else if (sender.hasPermission("sabercore.harvester.give")) {
                sender.sendMessage(translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("harvester-hoe.command-usage")));
            }
            return true;
        }
    }

    static class HarvesterHoesListener extends SaberPluginListener<HarvesterHoes> {
        public HarvesterHoesListener(HarvesterHoes patch) {
            super(patch);
        }

        @EventHandler
        public void onBreak(BlockBreakEvent event) {

            if (event.isCancelled())
                return;

            if (event.getPlayer().getItemInHand().getType() != Material.DIAMOND_HOE
                    || event.getPlayer().getItemInHand() == null
                    || !event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(translate(SaberCore.getInstance().getFileManager().getConfig().fetchString("settings.harvester-hoe.item.displayname"))))
                return;


            if (EconomyUtils.isHooked() && SaberCore.getInstance().getFileManager().getConfig().fetchBoolean("settings.harvester-hoe.autosell")) {
                if (event.getBlock().getType() == XMaterial.SUGAR_CANE.parseMaterial()) {
                    event.setCancelled(true);
                    Location currLoc = event.getBlock().getLocation();
                    while (currLoc.getBlock().getType() == XMaterial.SUGAR_CANE.parseMaterial()) {
                        currLoc = new Location(currLoc.getWorld(), currLoc.getBlockX(), currLoc.getBlockY() + 1, currLoc.getBlockZ());
                    }
                    currLoc = new Location(currLoc.getWorld(), currLoc.getBlockX(), currLoc.getBlockY() - 1, currLoc.getBlockZ());
                    while (currLoc.getBlockY() >= event.getBlock().getY()) {
                        currLoc.getBlock().setType(Material.AIR);
                        EconomyUtils.sell(event.getPlayer(), SaberCore.getInstance().getFileManager().getConfig().fetchDouble("settings.harvester-hoe.sugar-cane-price"));
                        currLoc = new Location(currLoc.getWorld(), currLoc.getBlockX(), currLoc.getBlockY() - 1, currLoc.getBlockZ());
                    }
                }
            } else {
                if (event.getBlock().getType() == XMaterial.SUGAR_CANE.parseMaterial()) {
                    event.setCancelled(true);
                    Location currLoc = event.getBlock().getLocation();
                    while (currLoc.getBlock().getType() == XMaterial.SUGAR_CANE.parseMaterial()) {
                        currLoc = new Location(currLoc.getWorld(), currLoc.getBlockX(), currLoc.getBlockY() + 1, currLoc.getBlockZ());
                    }
                    currLoc = new Location(currLoc.getWorld(), currLoc.getBlockX(), currLoc.getBlockY() - 1, currLoc.getBlockZ());
                    while (currLoc.getBlockY() >= event.getBlock().getY()) {
                        currLoc.getBlock().setType(Material.AIR);
                        givePlayerItem(event.getPlayer(), Material.SUGAR_CANE);
                        currLoc = new Location(currLoc.getWorld(), currLoc.getBlockX(), currLoc.getBlockY() - 1, currLoc.getBlockZ());
                    }
                }
            }
        }


        private void givePlayerItem(Player player, Material m) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(new ItemStack(m));
            } else if (getSlot(player, m) != -1) {
                player.getInventory().addItem(new ItemStack(m));
            } else {
                player.getWorld().dropItem(player.getLocation(), new ItemStack(m));
            }
        }

        private int getSlot(Player p, Material m) {
            for (int i = 0; i < p.getInventory().getSize(); i++) {
                if ((p.getInventory().getItem(i).getType() == m) && (p.getInventory().getItem(i).getAmount() < p.getInventory().getItem(i).getMaxStackSize())) {
                    return i;
                }
            }
            return -1;
        }

    }
}
