package org.aussie.anarchy.util.hook;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginHook<P extends JavaPlugin> {
    protected final P plugin;
    private final String name;

    public PluginHook(P plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Plugin getPlugin() {
        return this.plugin.getServer().getPluginManager().getPlugin(this.name);
    }
}
