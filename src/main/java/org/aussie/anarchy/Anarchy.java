package org.aussie.anarchy;

import org.aussie.anarchy.command.CommandManager;
import org.aussie.anarchy.hook.HookManager;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.ModuleManager;
import org.aussie.anarchy.util.IAnarchy;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

// 
public final class Anarchy extends JavaPlugin implements IAnarchy {

    private static Anarchy plugin;
    private Long startMillis;
    private BukkitScheduler scheduler;
    private CommandManager commandManager;
    private ModuleManager moduleManager;
    private HookManager hookManager;

    public Anarchy() {
    }

    public void onEnable() {
        this.log("                                _           ");
        this.log("     /\\                        | |          ");
        this.log("    /  \\   _ __   __ _ _ __ ___| |__  _   _ ");
        this.log("   / /\\ \\ | '_ \\ / _` | '__/ __| '_ \\| | | |");
        this.log("  / ____ \\| | | | (_| | | | (__| | | | |_| |");
        this.log(" /_/    \\_\\_| |_|\\__,_|_|  \\___|_| |_|\\__, |");
        this.log("                                       __/ |");
        this.log("                                      |___/ ");
        plugin = this;
        this.startMillis = System.currentTimeMillis();
        this.scheduler = plugin.getServer().getScheduler();
        this.processConfigs(this.getDataFolder());
        this.log(ChatColor.DARK_GREEN + "[Config] Loaded config");
        this.log(ChatColor.GOLD + "[Hooks] Loading hooks");
        this.hookManager = new HookManager(this);
        this.log(ChatColor.GOLD + "[Commands] Loading commands");
        this.commandManager = new CommandManager(this);
        this.log(ChatColor.GOLD + "[Features] Loading patches/features");
        this.moduleManager = new ModuleManager(this);
        this.log(ChatColor.DARK_GREEN + "[Anarchy] Anarchy successfully enabled! (" + (System.currentTimeMillis() - this.startMillis) + "ms)");
    }

    public void onDisable() {
        ((ProtocolLibHook) this.hookManager.getHook(ProtocolLibHook.class)).remove(plugin);
    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public static Anarchy getPlugin() {
        return plugin;
    }

    public Long getStartMillis() {
        return this.startMillis;
    }

    public BukkitScheduler getScheduler() {
        return this.scheduler;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public HookManager getHookManager() {
        return this.hookManager;
    }
}


