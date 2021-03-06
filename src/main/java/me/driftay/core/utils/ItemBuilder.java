package me.driftay.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
public class ItemBuilder {

    private final ItemMeta meta;
    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int durability) {
        this(new ItemStack(material, amount, (short) durability));
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }


    public ItemBuilder enchant(Enchantment enchantment, int level) {
        if (enchantment != null) {
            meta.addEnchant(enchantment, level, false);
        }
        return this;
    }

    public ItemBuilder durability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        if (lore != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (String line : lore) {
                arrayList.add(translate(line));
            }
            this.meta.setLore(arrayList);
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        meta.setLore(translate(lore));
        return this;
    }

    public ItemBuilder lore(List<String> lore, Placeholder... placeholders) {
        List<String> placeholderLore = new ArrayList<>();
        for (String line : lore) {
            for (Placeholder placeholder : placeholders) {
                line = line.replace(placeholder.getKey(), placeholder.getValue());
            }
            placeholderLore.add(line);
        }
        meta.setLore(translate(placeholderLore));
        return this;
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemBuilder addLineToLore(String line) {
        List<String> lore = meta.getLore();
        lore.add(translate(line));
        meta.setLore(lore);
        return this;
    }


}
