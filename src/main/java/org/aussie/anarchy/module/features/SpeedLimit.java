package org.aussie.anarchy.module.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class SpeedLimit extends Module {
    private final HashMap<Entity, Location> vehicleLocs = new HashMap<>();
    private final HashMap<Player, Location> locations = new HashMap<>();
    private final Set<Player> teleported = new HashSet<>();
    private double maxSpeed;

    @Override
    public boolean isEnabled() {
        return Config.SPEEDLIMIT;
    }

    @Override
    public Module onEnable() {
        Anarchy.getScheduler().scheduleSyncRepeatingTask(Anarchy.getPlugin(), new Checker(locations, teleported, maxSpeed), 0L, 20L);
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                if (p.isInsideVehicle() && p.getVehicle() != null) {
                    Entity vehicle = p.getVehicle();
                    if (SpeedLimit.this.vehicleLocs.get(vehicle) != null) {
                        Location prevLocation = SpeedLimit.this.vehicleLocs.get(vehicle).clone();
                        Location newLocation = vehicle.getLocation().clone();
                        Vector vector = newLocation.subtract(prevLocation).toVector();
                        if (vector.clone().normalize().getY() < -0.95D) {
                            SpeedLimit.this.vehicleLocs.remove(vehicle);
                        }

                        if (vector.length() > SpeedLimit.this.maxSpeed) {
                            List<Entity> player = vehicle.getPassengers();

                            if (vehicle.eject()) {
                                vehicle.teleport(prevLocation.clone().add(0.0D, 0.5D, 0.0D));
                            }

                            player.forEach((entity) -> entity.teleport(prevLocation));
                        }
                    }

                    SpeedLimit.this.vehicleLocs.put(vehicle, vehicle.getLocation());
                }

            }
        });
        return this;
    }

    public static class Checker implements Runnable {

        private final HashMap<Player, Location> locations;
        private final Set<Player> teleported;
        private double maxSpeed;

        public Checker(HashMap<Player, Location> locations, Set<Player> teleported, double maxSpeed) {
            this.locations = locations;
            this.teleported = teleported;
            this.maxSpeed = maxSpeed;
        }


        @Override
        public void run() {
            this.maxSpeed = Config.SPEEDLIMITMAXBPS / 20.0D * Util.getTPS();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player == null) {
                    return;
                }

                Entity vehicle = player.getVehicle();
                if (!this.teleported.contains(player) && this.locations.get(player) != null) {
                    Location prevLocation = this.locations.get(player).clone();
                    Location newLocation = player.getLocation().clone();
                    Vector vector = newLocation.subtract(prevLocation).toVector();
                    if (vector.clone().normalize().getY() < -0.95D) {
                        this.locations.remove(player);
                        continue;
                    }

                    if (vector.length() > this.maxSpeed) {
                        if (player.isInsideVehicle() && vehicle != null && vehicle.eject()) {
                            vehicle.teleport(prevLocation.clone().add(0.0D, 0.5D, 0.0D));
                        }

                        player.teleport(prevLocation);
                        continue;
                    }
                }

                this.locations.put(player, player.getLocation().clone());
                this.teleported.remove(player);
            }
        }
    }

    @EventHandler
    private void on(PlayerRespawnEvent e) {
        this.teleported.add(e.getPlayer());
    }

    @EventHandler
    private void on(PlayerTeleportEvent e) {
        this.teleported.add(e.getPlayer());
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        this.locations.remove(e.getPlayer());
        this.teleported.remove(e.getPlayer());
    }
}
