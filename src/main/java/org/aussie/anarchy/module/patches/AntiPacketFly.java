package org.aussie.anarchy.module.patches;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AntiPacketFly extends Module {
    private final HashMap<UUID, Integer> vl = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTIPACKETFLY;
    }

    @Override
    public Module onEnable() {
        get().getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(get(), ListenerPriority.HIGHEST, PacketType.Play.Client.TELEPORT_ACCEPT) {
            public void onPacketReceiving(PacketEvent event) {
                Player e = event.getPlayer();
                if (!e.isGliding() && !e.isInsideVehicle() && event.getPacketType() == PacketType.Play.Client.TELEPORT_ACCEPT) {
                    Material b = e.getLocation().getBlock().getType();

                    boolean solid = !b.equals(Material.AIR) && (b.isOccluding() || b.isSolid());

                    if (!Config.ANTIPACKETFLYONLYSOLID || solid) {
                        if (AntiPacketFly.this.vl.get(e.getUniqueId()) != null) {
                            if (AntiPacketFly.this.vl.get(e.getUniqueId()) > Config.MAXPACKETFLYVL) {
                                event.setCancelled(true);
                            } else {
                                AntiPacketFly.this.vl.merge(e.getUniqueId(), 1, Integer::sum);
                                Module.get().getScheduler().runTaskLater(this.plugin, () -> AntiPacketFly.this.vl.put(e.getUniqueId(), AntiPacketFly.this.vl.get(e.getUniqueId()) - 1), 200L);
                            }
                        } else {
                            AntiPacketFly.this.vl.put(e.getUniqueId(), 1);
                            Module.get().getScheduler().runTaskLater(this.plugin, () -> AntiPacketFly.this.vl.put(e.getUniqueId(), AntiPacketFly.this.vl.get(e.getUniqueId()) - 1), 200L);
                        }
                    }
                }
            }
        });
        return this;
    }
}
