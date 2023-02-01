package au.twobeetwotee.anarchy;

import au.twobeetwotee.anarchy.commands.CommandManager;
import au.twobeetwotee.anarchy.patches.ModuleManager;
import au.twobeetwotee.anarchy.utils.IAnarchy;
import au.twobeetwotee.anarchy.utils.Util;
import lombok.Getter;
import au.twobeetwotee.anarchy.utils.hook.HookManager;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Objects;

public class AnarchyPatches extends JavaPlugin implements IAnarchy {

    @Getter
    private static Long startMillis;

    @Getter
    private static AnarchyPatches plugin;
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
        processConfigs();
        this.log(ChatColor.DARK_GREEN + "[Config] Loaded config");
        this.log(ChatColor.GOLD + "[Hooks] Loading hooks");
        hookManager = new HookManager(this);
        this.log(ChatColor.GOLD + "[Commands] Loading commands");
        commandManager = new CommandManager(this);
        this.log(ChatColor.GOLD + "[Features] Loading patches/features");
        moduleManager = new ModuleManager(this);
        this.log(ChatColor.DARK_GREEN + "[AnarchyPatches] AnarchyPatches successfully enabled! (" + (System.currentTimeMillis() - startMillis) + "ms)");
    }

    public void processConfigs() {
        this.processConfigs(this.getDataFolder());
    }

    public void onDisable() {
        hookManager.getHook(ProtocolLibHook.class).remove(plugin);
    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public static String lastMessage = "";

    public static void logToAdmins(String message) {
        if (!Objects.equals(lastMessage, message)) {
            Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(Util::isAdmin)
                    .forEach(player -> player.sendMessage("§6" + message));

            getPlugin().log(message);

            lastMessage = message;
        }
    }

}
