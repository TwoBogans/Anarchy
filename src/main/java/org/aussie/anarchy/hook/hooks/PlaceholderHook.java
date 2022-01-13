package org.aussie.anarchy.hook.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.hook.PluginHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends PluginHook<Anarchy> {
    public static final String NAME = "PlaceholderAPI";
    private boolean papiEnabled;

    public PlaceholderHook(Anarchy plugin) {
        super(plugin, "PlaceholderAPI");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            plugin.log(ChatColor.RED + " Couldn't detect " + "PlaceholderAPI");
        } else {
            (new PlaceholderHook.Placeholders()).register();
            this.papiEnabled = true;
        }
    }

    public boolean isPapiEnabled() {
        return this.papiEnabled;
    }

    public static class Placeholders extends PlaceholderExpansion {
        public Placeholders() {
        }

        @NotNull
        public String getIdentifier() {
            return "anarchy";
        }

        @NotNull
        public String getAuthor() {
            return "2b2tau";
        }

        @NotNull
        public String getVersion() {
            return "1.0";
        }
    }
}
