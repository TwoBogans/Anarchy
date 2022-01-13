package org.aussie.anarchy.util.hook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHookManager<P extends JavaPlugin> {
    protected final P plugin;
    private final Map<Class<? extends PluginHook<P>>, PluginHook<P>> hooks = new HashMap();

    public AbstractHookManager(P plugin) {
        this.plugin = plugin;
    }

    protected void register(String name, Class<? extends PluginHook<P>> clazz) {
        Plugin target = Bukkit.getPluginManager().getPlugin(name);
        if (target != null && target.isEnabled()) {
            try {
                if (this.hooks.putIfAbsent(clazz, clazz.getConstructor(this.plugin.getClass()).newInstance(this.plugin)) != null) {
                    this.plugin.getLogger().warning(ChatColor.RED + "[Hooks] Failed to hook into " + name + " : There was already a hook registered with same name");
                    return;
                }

                this.plugin.getLogger().info(ChatColor.DARK_GREEN + "[Hooks] Hooked into " + name);
            } catch (Throwable var5) {
                Throwable throwable = var5;
                if (var5.getCause() != null) {
                    throwable = var5.getCause();
                }

                this.plugin.getLogger().warning(ChatColor.RED + "[Hooks] Failed to hook into " + name + " : " + throwable.getMessage());
            }

        }
    }

    public <T extends PluginHook<P>> T getHook(Class<T> clazz) {
        return clazz != null ? clazz.cast(this.hooks.get(clazz)) : null;
    }
}
