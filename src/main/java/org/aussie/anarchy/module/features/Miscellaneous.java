package org.aussie.anarchy.module.features;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Miscellaneous extends Module {
    public Miscellaneous() {
    }

    public boolean isEnabled() {
        return true;
    }

    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(EntityDamageByEntityEvent event) {
        if (Config.CRYSTALDELAY != -1) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof EnderCrystal && event.getEntity().getTicksLived() < Config.CRYSTALDELAY) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void on(PlayerKickEvent e) {
        e.setReason(ChatColor.translateAlternateColorCodes('&', Messages.KICK_MESSAGE));
        this.forceGamemode(e.getPlayer());
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void on(PlayerJoinEvent e) {
        this.forceGamemode(e.getPlayer());
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void on(PlayerQuitEvent e) {
        this.forceGamemode(e.getPlayer());
    }

    private void forceGamemode(Player p) {
        if (!Util.isAdmin(p)) {
            p.setOp(false);
            p.setGameMode(GameMode.SURVIVAL);
        }

    }
}
