package org.aussie.anarchy.module.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayServerNamedSoundEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;

import java.awt.*;

public class NoGlobalThunder extends Module {

    @Override
    public boolean isEnabled() {
        return Config.NOGLOBALTHUNDER;
    }

    @Override
    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            public void onPacketSending(PacketEvent e) {
                WrapperPlayServerNamedSoundEffect packet = new WrapperPlayServerNamedSoundEffect(e.getPacket());

                Sound sound = packet.getSoundEffect();
                Sound thunder = CompatUtil.is1_12() ? Sound.valueOf("ENTITY_LIGHTNING_THUNDER") : Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
                Sound impact = CompatUtil.is1_12() ? Sound.valueOf("ENTITY_LIGHTNING_IMPACT") : Sound.ENTITY_LIGHTNING_BOLT_IMPACT;

                if (sound.compareTo(thunder) > 0 || sound.compareTo(impact) > 0) {
                    int x = packet.getEffectPositionX() / 8;
                    int z = packet.getEffectPositionZ() / 8;

                    int x1 = e.getPlayer().getLocation().getBlockX();
                    int z1 = e.getPlayer().getLocation().getBlockZ();

                    Point p1 = new Point(x, z);
                    Point p2 = new Point(x1, z1);

                    if (p1.distance(p2) > (double) (Bukkit.getViewDistance() * 16 - 2)) {
                        e.setCancelled(true);
                    }
                }
            }
        });
        return this;
    }
}