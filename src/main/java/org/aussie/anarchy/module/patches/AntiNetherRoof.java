package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class AntiNetherRoof extends Module {
    @Override
    public boolean isEnabled() {
        return Config.ANTINETHERROOF;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location l = p.getLocation();
        if (!p.isOp()) {
            if (l.getWorld().getName().equals("world_nether") && l.getY() >= 125.0D) {
                if (p.isInsideVehicle()) {
                    p.leaveVehicle();
                }

                if (p.isFlying() || p.isGliding()) {
                    e.setCancelled(true);
                }

                Location newLoc = new Location(l.getWorld(), l.getBlockX(), 120, l.getBlockZ());

                if (newLoc.getBlock().getType() != Material.AIR) {
                    newLoc.getBlock().setType(Material.AIR);
                }

                Location newLocUp = newLoc.add(0, 1, 0);

                if (newLocUp.getBlock().getType() != Material.AIR) {
                    newLocUp.getBlock().setType(Material.AIR);
                }

                e.setTo(newLoc);
            }

        }
    }

    @EventHandler
    public void on(EntityToggleGlideEvent e) {
        Player p = (Player)e.getEntity();
        Location l = p.getLocation();
        if (!p.isOp()) {
            if (l.getWorld().getName().equals("world_nether") && l.getY() >= 125.0D) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(VehicleMoveEvent e) {
        Location l = e.getVehicle().getLocation();
        if (l.getWorld().getName().equals("world_nether") && l.getY() >= 125.0D) {
            e.getVehicle().eject();
        }
    }
}
