package org.aussie.anarchy.module.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class WitherSound extends Module {

    @Override
    public boolean isEnabled() {
        return Config.WITHERSOUND;
    }

    @Override
    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.WORLD_EVENT) {
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                if (packet.getIntegers().read(0) == 1023) {
                    packet.getBooleans().write(0, false);
                }

            }
        });
        return this;
    }

    @EventHandler
    private void on(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.WITHER) {
            for (Player p : Anarchy.getPlugin().getServer().getOnlinePlayers()) {
                if (!e.getLocation().getWorld().equals(p.getWorld())) continue;
                if (e.getEntity().getLocation().distance(p.getLocation()) <= (double) (Bukkit.getViewDistance() * 16 - 2)) {
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
                }
            }
        }
    }
}