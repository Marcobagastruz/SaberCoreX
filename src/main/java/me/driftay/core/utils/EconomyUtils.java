package me.driftay.core.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/25/2020
 */
public class EconomyUtils {

    public static Economy economy;

    public static void register() {
        RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (service != null) {
            economy = service.getProvider();
        }
    }

    public static void sell(Player player, double price) {
        economy.depositPlayer(player, price);
    }

    public static boolean isHooked() {
        return economy != null;
    }


}
