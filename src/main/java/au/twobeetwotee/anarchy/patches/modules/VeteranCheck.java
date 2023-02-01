package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.LuckPermsHook;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class VeteranCheck extends Module {
    static LuckPermsHook luckPerms;

    public boolean isEnabled() {
        return Config.VETCHECK;
    }

    public Module onEnable() {
        luckPerms = AnarchyPatches.getHookManager().getHook(LuckPermsHook.class);
        return this;
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        checkVeteran(e.getPlayer());
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        checkVeteran(e.getPlayer());
    }

    public static void checkVeteran(Player player) {
        UUID uuid = player.getUniqueId();
        if (!luckPerms.hasPermission(uuid, "group.veteran") || !isVeteran(player)) {
            if (!Bukkit.isPrimaryThread()) {
                AnarchyPatches.getScheduler().runTask(AnarchyPatches.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + uuid + " parent " + (isVeteran(player) ? "add" : "remove") + " veteran"));
            }

        }
    }

    public static boolean isVeteran(OfflinePlayer player) {
        return player.getFirstPlayed() <= Config.VETTIME || Config.FORCEVET.contains(player.getUniqueId().toString()) || Util.isAdmin(player.getUniqueId());
    }
}
