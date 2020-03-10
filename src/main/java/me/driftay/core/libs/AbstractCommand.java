package me.driftay.core.libs;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public abstract class AbstractCommand implements TabExecutor {
    public static CommandMap cmap;
    public final String command;
    protected final String description;
    protected final List<String> alias;
    protected final String usage;
    protected final String permMessage;

    public AbstractCommand(String command) {
        this(command, null, null, null, null);
    }

    public AbstractCommand(String command, String usage) {
        this(command, usage, null, null, null);
    }

    public AbstractCommand(String command, String usage, String description) {
        this(command, usage, description, null, null);
    }

    public AbstractCommand(String command, String usage, String description, String permissionMessage) {
        this(command, usage, description, permissionMessage, null);
    }

    public AbstractCommand(String command, String usage, String description, List<String> aliases) {
        this(command, usage, description, null, aliases);
    }

    public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
        this.command = command.toLowerCase();
        this.usage = usage;
        this.description = description;
        this.permMessage = permissionMessage;
        this.alias = aliases;
    }

    public Command register() {
        AbstractCommand.ReflectCommand cmd = new ReflectCommand(this.command);
        if (this.alias != null) {
            cmd.setAliases(this.alias);
        }

        if (this.description != null) {
            cmd.setDescription(this.description);
        }

        if (this.usage != null) {
            cmd.setUsage(this.usage);
        }

        if (this.permMessage != null) {
            cmd.setPermissionMessage(this.permMessage);
        }

        this.getCommandMap().register("saber", cmd);
        cmd.setExecutor(this);
        return cmd;
    }

    final CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return this.getCommandMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cmap != null) {
            return cmap;
        }

        return this.getCommandMap();
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    private static final class ReflectCommand extends Command {
        private AbstractCommand exe = null;

        protected ReflectCommand(String command) {
            super(command);
        }

        public void setExecutor(AbstractCommand exe) {
            this.exe = exe;
        }

        public boolean execute(@NotNull CommandSender sender, String commandLabel, String[] args) {
            return this.exe != null && this.exe.onCommand(sender, this, commandLabel, args);
        }

        public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
            return this.exe != null ? this.exe.onTabComplete(sender, this, alias, args) : null;
        }
    }
}
