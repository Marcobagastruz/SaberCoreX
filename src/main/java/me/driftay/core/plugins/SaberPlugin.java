package me.driftay.core.plugins;

import me.driftay.core.libs.AbstractCommand;
import me.driftay.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public abstract class SaberPlugin {
    protected static Random random = new Random();
    public long enable_time = 0L;
    public boolean disableCalled = false;
    private List<Listener> listeners = new ArrayList<>();
    private List<Integer> task_ids = new ArrayList<>();
    private List<Command> commands = new ArrayList<>();
    private Plugin bukkit_plugin = null;
    private boolean running = false;

    public SaberPlugin(Plugin p) {
        this.bukkit_plugin = p;
    }

    public void enable() {
        this.disableCalled = false;
    }

    public void disable() {
        this.disableCalled = true;
    }

    public void inject() {
        this.running = true;
        this.enable_time = System.currentTimeMillis();
        try {
            this.enable();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[SaberCoreX]" + ChatColor.WHITE.toString() + " Injected " + ChatColor.YELLOW.toString() + this.getClass().getSimpleName() + ChatColor.WHITE.toString() + " module.");
        } catch (Exception e) {
            e.printStackTrace();
            this.kill();
        }
    }

    public void kill() {
        try {
            this.disable();
            this.unregisterListeners();
            this.unregisterTasks();
            this.unregisterCommands();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[SaberCoreX]" + ChatColor.WHITE.toString() + " Killed " + ChatColor.RED.toString() + this.getClass().getSimpleName() + ChatColor.WHITE.toString() + " module.");
            this.enable_time = 0L;
            this.running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void registerListener(Listener l) {
        this.listeners.add(l);
        Bukkit.getPluginManager().registerEvents(l, this.bukkit_plugin);
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Registered listener: " + l.getClass().getSimpleName() + "!");
    }

    public void registerTask(BukkitTask bt, Class<?> c) {
        this.task_ids.add(bt.getTaskId());
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Registered task: " + c.getSimpleName() + "!");
    }


    public void registerCommand(AbstractCommand acmd) {
        acmd.register();
        this.commands.add(AbstractCommand.cmap.getCommand(acmd.command));
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Registered command: /" + acmd.command + "!");
    }

    public void unregisterCommand(Command pcmd) {
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Unregistered command: /" + pcmd.getName() + "!");
        CommandUtils.unRegisterBukkitCommand(pcmd);
    }

    public void unregisterListeners() {
        for (Listener listener : this.listeners) {
            Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Unregistered listener: " + listener.getClass().getSimpleName() + "!");
            HandlerList.unregisterAll(listener);
        }

        this.listeners.clear();
    }

    public void unregisterTasks() {
        for (int i : this.task_ids) {
            Bukkit.getScheduler().cancelTask(i);
            Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Unregistered task: #" + i + "!");
        }

        this.task_ids.clear();
    }

    public void unregisterCommands() {
        for (Command pcmd : this.commands) {
            this.unregisterCommand(pcmd);
        }
        this.commands.clear();
    }

    public Plugin getBukkitPlugin() {
        return this.bukkit_plugin;
    }

    public boolean isRunning() {
        return this.running;
    }
}

