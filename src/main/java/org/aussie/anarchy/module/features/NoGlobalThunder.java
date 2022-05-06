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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;

public class NoGlobalThunder extends Module {

    @Override
    public boolean isEnabled() {
        return Config.NOGLOBALTHUNDER;
    }

    @Override
    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            public void onPacketSending(PacketEvent e) {
                if (e.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                    PacketContainer packet = e.getPacket();
                    Sound sound = packet.getSoundEffects().read(0);
                    Sound thunder = CompatUtil.is1_12() ? Sound.valueOf("ENTITY_LIGHTNING_THUNDER") : Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
                    Sound impact = CompatUtil.is1_12() ? Sound.valueOf("ENTITY_LIGHTNING_IMPACT") : Sound.ENTITY_LIGHTNING_BOLT_IMPACT;
                    if (sound.compareTo(thunder) > 0 || sound.compareTo(impact) > 0) {
                        int x = packet.getIntegers().read(0) / 8;
                        int z = packet.getIntegers().read(2) / 8;
                        Location loc = e.getPlayer().getLocation();
                        double distance = NoGlobalThunder.this.distanceBetweenPoints(x, loc.getBlockX(), z, loc.getBlockZ());
                        if (distance > (double)(Bukkit.getViewDistance() * 16 - 2)) {
                            e.setCancelled(true);
                        }
                    }
                }

            }
        });
        return this;
    }

    private double distanceBetweenPoints(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}