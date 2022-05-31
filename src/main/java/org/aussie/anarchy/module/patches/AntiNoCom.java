package org.aussie.anarchy.module.patches;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayClientBlockDig;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayClientUseItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AntiNoCom extends Module {

    @Override
    public boolean isEnabled() {
        return Config.ANTINOCOM && CompatUtil.is1_12();
    }

    @Override
    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ITEM) {
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(event.getPacket());

                BlockPosition blockPos = packet.getLocation();
                Location location = event.getPlayer().getLocation();

                double distance = location.distance(blockPos.toLocation(location.getWorld()));

                if (distance > Bukkit.getViewDistance() * 16 - 2) {
                    packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                    event.setCancelled(true);
                }
            }
        });

        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientBlockDig packet = new WrapperPlayClientBlockDig(event.getPacket());

                BlockPosition blockPos = packet.getLocation();

                double distance = event.getPlayer().getLocation().distance(blockPos.toLocation(event.getPlayer().getWorld()));

                if (distance > Bukkit.getViewDistance() * 16 - 2) {
                    event.setCancelled(true);
                }
            }
        });
        return this;
    }
}
