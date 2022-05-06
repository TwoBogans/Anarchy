package org.aussie.anarchy.module.patches;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayServerSpawnEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.HashSet;
import java.util.UUID;

public class AntiLag extends Module {
    private final HashSet<UUID> vl = new HashSet<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTILAG;
    }

    @Override
    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.SPAWN_ENTITY) {
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                WrapperPlayServerSpawnEntity wrapperPlayServerSpawnEntity = new WrapperPlayServerSpawnEntity(packet);
                Entity e = wrapperPlayServerSpawnEntity.getEntity(event.getPlayer().getWorld());
                int count = 0;
                Entity[] var6 = e.getChunk().getEntities();

                for (Entity ce : var6) {
                    if (ce instanceof Firework) {
                        ++count;
                    }
                }

                if (count > Config.MAXFIREWORKS) {
                    event.setCancelled(true);
                }

            }
        });
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.AUTO_RECIPE) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.AUTO_RECIPE) {
                    if (AntiLag.this.vl.contains(event.getPlayer().getUniqueId())) {
                        event.setCancelled(true);
                    } else {
                        AntiLag.this.vl.add(event.getPlayer().getUniqueId());
                        Bukkit.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                            AntiLag.this.vl.remove(event.getPlayer().getUniqueId());
                        }, 1L);
                    }
                }

            }
        });
        return this;
    }

    @EventHandler
    private void on(VehicleCreateEvent event) {
        if (this.isEnabled()) {
            try {
                Chunk chunk = event.getVehicle().getChunk();
                int amount = 0;
                Entity[] var4 = chunk.getEntities();
                int var5 = var4.length;

                int var6;
                Entity ent;
                for(var6 = 0; var6 < var5; ++var6) {
                    ent = var4[var6];
                    if (ent instanceof Vehicle) {
                        ++amount;
                    }
                }

                if (amount >= Config.MAXMINECARTS) {
                    event.setCancelled(true);
                    var4 = chunk.getEntities();
                    var5 = var4.length;

                    for(var6 = 0; var6 < var5; ++var6) {
                        ent = var4[var6];
                        if (!(ent instanceof Player)) {
                            ent.remove();
                        }
                    }
                }
            } catch (Exception | Error var8) {
                System.out.println(var8);
            }
        }

    }

    @EventHandler
    private void on(VehicleMoveEvent e) {
        if (this.isEnabled()) {
            Chunk chunk = e.getVehicle().getChunk();
            Vehicle vehicle = e.getVehicle();
            if (!e.getFrom().getChunk().equals(e.getTo().getChunk()) && chunk.getEntities().length >= Config.MAXMINECARTS) {
                vehicle.remove();
            }
        }

    }
}
