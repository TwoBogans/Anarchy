package org.aussie.anarchy.module.features;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.LuckPermsHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
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
        luckPerms = Anarchy.getHookManager().getHook(LuckPermsHook.class);
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
                Anarchy.getScheduler().runTask(Anarchy.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + uuid + " parent " + (isVeteran(player) ? "add" : "remove") + " veteran"));
            }

        }
    }

    public static boolean isVeteran(OfflinePlayer player) {
        return player.getFirstPlayed() <= Config.VETTIME || Config.FORCEVET.contains(player.getUniqueId().toString()) || Util.isAdmin(player.getUniqueId());
    }
}
