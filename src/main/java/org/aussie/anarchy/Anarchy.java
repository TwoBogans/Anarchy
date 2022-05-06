package org.aussie.anarchy;

import lombok.Getter;
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

    @Getter
    private static Long startMillis;

    @Getter
    private static Anarchy plugin;
    @Getter
    private static BukkitScheduler scheduler;
    @Getter
    private static CommandManager commandManager;
    @Getter
    private static ModuleManager moduleManager;
    @Getter
    private static HookManager hookManager;

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
        startMillis = System.currentTimeMillis();
        scheduler = plugin.getServer().getScheduler();
        this.processConfigs(this.getDataFolder());
        this.log(ChatColor.DARK_GREEN + "[Config] Loaded config");
        this.log(ChatColor.GOLD + "[Hooks] Loading hooks");
        hookManager = new HookManager(this);
        this.log(ChatColor.GOLD + "[Commands] Loading commands");
        commandManager = new CommandManager(this);
        this.log(ChatColor.GOLD + "[Features] Loading patches/features");
        moduleManager = new ModuleManager(this);
        this.log(ChatColor.DARK_GREEN + "[Anarchy] Anarchy successfully enabled! (" + (System.currentTimeMillis() - startMillis) + "ms)");
    }

    public void onDisable() {
        hookManager.getHook(ProtocolLibHook.class).remove(plugin);
    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

}


