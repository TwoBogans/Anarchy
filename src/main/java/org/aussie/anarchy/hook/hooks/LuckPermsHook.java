package org.aussie.anarchy.hook.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.hook.PluginHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class LuckPermsHook extends PluginHook<Anarchy> {
    public static final String NAME = "LuckPerms";
    private LuckPerms luckPerms;

    public LuckPermsHook(Anarchy plugin) {
        super(plugin, "LuckPerms");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            plugin.warn("Couldn't detect LuckPerms");
        } else {
            this.luckPerms = provider.getProvider();
        }
    }

    public boolean hasPermission(UUID uuid, String permission) {
        return this.hasPermission(this.getUser(uuid), permission);
    }

    public boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    public User getUser(UUID uuid) {
        return this.luckPerms.getUserManager().getUser(uuid);
    }

    public LuckPerms getLuckPerms() {
        return this.luckPerms;
    }
}