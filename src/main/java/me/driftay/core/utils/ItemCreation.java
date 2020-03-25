package me.driftay.core.utils;

import me.driftay.core.SaberCore;
import me.driftay.core.utils.struct.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.driftay.core.utils.StringUtils.translate;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/15/2020
 */
public class ItemCreation {


    public static ItemStack giveHarvesterHoe(int amount) {
        ItemStack harvesterItem = new ItemStack(XMaterial.DIAMOND_HOE.parseMaterial());
        ItemMeta meta = harvesterItem.getItemMeta();
        meta.setDisplayName(translate(SaberCore.getInstance().getFileManager().getConfig().fetchString("settings.harvester-hoe.item.displayname")));
        List<String> lore = new ArrayList<>();
        for (String s : SaberCore.getInstance().getFileManager().getConfig().fetchStringList("settings.harvester-hoe.item.lore")) {
            lore.add(translate(s));
        }
        meta.setLore(lore);
        harvesterItem.setAmount(amount);
        harvesterItem.setItemMeta(meta);
        return harvesterItem;
    }


    public static ItemStack giveChunkBuster(int amount) {
        ItemStack busterItem = new ItemStack(XMaterial.END_PORTAL_FRAME.parseMaterial());
        ItemMeta meta = busterItem.getItemMeta();
        meta.setDisplayName(translate(SaberCore.getInstance().getFileManager().getConfig().fetchString("settings.chunkbuster.item.displayname")));
        List<String> lore = new ArrayList<>();
        for (String s : SaberCore.getInstance().getFileManager().getConfig().fetchStringList("settings.chunkbuster.item.lore")) {
            lore.add(translate(s));
        }
        meta.setLore(lore);
        busterItem.setAmount(amount);
        busterItem.setItemMeta(meta);
        return busterItem;
    }

}
