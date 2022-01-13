package org.aussie.anarchy.module.patches;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayClientBlockDig;

public class AntiNoCom extends Module {
    public AntiNoCom() {
    }

    public boolean isEnabled() {
        return Config.ANTINOCOM && CompatUtil.is1_12();
    }

    public Module onEnable() {
        get().getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(get(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ITEM) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
                    BlockPosition packetLocation = event.getPacket().getBlockPositionModifier().read(0);
                    if (event.getPlayer().getLocation().distance(packetLocation.toLocation(event.getPlayer().getWorld())) > (double)Config.MAXNOCOMDIST) {
                        event.setCancelled(true);
                    }
                }

            }
        });
        get().getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(get(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                WrapperPlayClientBlockDig wrappedPacket = new WrapperPlayClientBlockDig(packet);
                EnumWrappers.PlayerDigType type = wrappedPacket.getStatus();
                if (type.equals(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) || type.equals(EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) || type.equals(EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK)) {
                    BlockPosition blockPos = wrappedPacket.getLocation();
                    double distance = event.getPlayer().getLocation().distance(blockPos.toLocation(event.getPlayer().getWorld()));
                    if (distance > (double)Config.MAXNOCOMDIST) {
                        event.setCancelled(true);
                    }
                }

            }
        });
        return this;
    }
}
