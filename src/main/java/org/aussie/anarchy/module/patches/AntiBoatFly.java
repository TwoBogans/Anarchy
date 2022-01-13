package org.aussie.anarchy.module.patches;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AntiBoatFly extends Module {
    private final HashMap<UUID, Integer> vl = new HashMap<>();

    public AntiBoatFly() {
    }

    public boolean isEnabled() {
        return Config.ANTIBOATFLY;
    }

    public Module onEnable() {
        get().getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent event) {
                Player e = event.getPlayer();
                Location l = e.getLocation();
                if (event.getPlayer().getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ()).getType() != Material.WATER) {
                    if (e.isInsideVehicle() && e.getVehicle() != null) {
                        Entity vehicle = e.getVehicle();
                        if (AntiBoatFly.this.vl.get(e.getUniqueId()) != null) {
                            if (AntiBoatFly.this.vl.get(e.getUniqueId()) > Config.MAXBOATFLYVL) {
                                vehicle.remove();
                            } else {
                                AntiBoatFly.this.vl.merge(e.getUniqueId(), 1, Integer::sum);
                                Bukkit.getServer().getScheduler().runTaskLater(this.plugin, () -> AntiBoatFly.this.vl.put(e.getUniqueId(), AntiBoatFly.this.vl.get(e.getUniqueId()) - 1), 200L);
                            }
                        } else {
                            AntiBoatFly.this.vl.put(e.getUniqueId(), 1);
                            Bukkit.getServer().getScheduler().runTaskLater(this.plugin, () -> AntiBoatFly.this.vl.put(e.getUniqueId(), AntiBoatFly.this.vl.get(e.getUniqueId()) - 1), 200L);
                        }
                    }

                }
            }
        });
        return this;
    }
}
