package me.driftay.core.modules;

import me.driftay.core.SaberCore;
import me.driftay.core.libs.SaberPluginCommand;
import me.driftay.core.plugins.SaberPlugin;
import me.driftay.core.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/25/2020
 */
public class NightVision extends SaberPlugin {

    public static Set<UUID> nightVision = new HashSet<>();

    public NightVision(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        super.enable();
        this.registerCommand(new NightVisionCommand(this));
    }

    @Override
    public void disable() {
        super.disable();
        this.unregisterCommands();
    }

    static class NightVisionCommand extends SaberPluginCommand<NightVision> {

        public NightVisionCommand(NightVision patch) {
            super(patch, "nv");
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("sabercore.nightvision")) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.no-permission")));
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("general.must-be-player")));
                return true;
            }

            if (nightVision.contains(((Player) sender).getUniqueId())) {
                sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("night-vision.toggled-off")));
                nightVision.remove(((Player) sender).getUniqueId());
                ((Player) sender).removePotionEffect(PotionEffectType.NIGHT_VISION);
                return true;
            }

            sender.sendMessage(StringUtils.translate(SaberCore.getInstance().getFileManager().getMessages().fetchString("night-vision.toggled-on")));
            nightVision.add(((Player) sender).getUniqueId());
            ((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

            return true;
        }
    }
}

