package me.driftay.core.hooks.impl;


import me.driftay.core.SaberCore;
import me.driftay.core.hooks.PluginHook;
import me.driftay.core.hooks.exception.NotImplementedException;
import me.driftay.core.hooks.impl.factions.FactionMCHook;
import me.driftay.core.hooks.impl.factions.FactionUUIDHook;
import me.driftay.core.utils.Logger;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionHook implements PluginHook<FactionHook> {

    @Override
    public FactionHook setup() {
        if (SaberCore.getInstance().getServer().getPluginManager().getPlugin(getName()) == null) {
            Logger.print("Factions could not be found", Logger.PrefixType.WARNING);
            return null;
        }
        List<String> authors = SaberCore.instance.getServer().getPluginManager().getPlugin(getName()).getDescription().getAuthors();
        if (!authors.contains("drtshock") && !authors.contains("Benzimmer")) {
            Logger.print("Server Factions type has been set to (MassiveCore)", Logger.PrefixType.DEFAULT);
            return new FactionMCHook();
        } else {
            Logger.print("Server Factions type has been set to (FactionsUUID/SaberFactions)", Logger.PrefixType.DEFAULT);
            return new FactionUUIDHook();
        }
    }

    public boolean canBuild(Block block, Player player) {
        try {
            throw new NotImplementedException("Factions does not exist!");
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getName() {
        return "Factions";
    }

}
