package au.twobeetwotee.anarchy.utils.hook.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.PluginHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class LuckPermsHook extends PluginHook<AnarchyPatches> {
    public static final String NAME = "LuckPerms";
    private LuckPerms luckPerms;

    public LuckPermsHook(AnarchyPatches plugin) {
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