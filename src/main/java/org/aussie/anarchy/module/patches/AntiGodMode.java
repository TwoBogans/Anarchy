package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class AntiGodMode extends Module {

    @Override
    public boolean isEnabled() {
        return Config.ANTIGODMODE;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getDamage() > (double)Config.MAXDAMAGE) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location from = e.getFrom();
        if (p.isInsideVehicle() && !Objects.requireNonNull(p.getVehicle()).isValid()) {
            p.getVehicle().eject();
            e.setTo(from);
        }

        if (!p.isValid() && !p.isDead()) {
            p.kickPlayer("");
        }
    }
}
