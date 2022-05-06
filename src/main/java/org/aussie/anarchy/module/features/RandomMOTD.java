package org.aussie.anarchy.module.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.apache.commons.lang.WordUtils;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Motds;
import org.aussie.anarchy.util.packet.wrappers.WrapperHandshakingClientSetProtocol;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class RandomMOTD extends Module {

    private final HashMap<String, Boolean> domainMap = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Motds.ENABLED;
    }

    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Handshake.Client.SET_PROTOCOL) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                final WrapperHandshakingClientSetProtocol wrappedPacket = new WrapperHandshakingClientSetProtocol(packet);
                final String domain = wrappedPacket.getServerAddressHostnameOrIp().toLowerCase();

                System.out.printf("[DEBUG] [RandomMOTD] Domain: %s", domain);

                if(domain.contains("aussie")) {
                    domainMap.put(domain, true);
                } else {
                    domainMap.put(domain, false);
                }
            }
        });
        return this;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(ServerListPingEvent event) {
        event.setMotd(getFormattedMotd());
    }

    public static String getRandomMotd() {
        return Motds.MOTDS.get(random.nextInt(Motds.MOTDS.size())).replaceAll("%name%", getRandomName());
    }

    public static String getRandomName() {
        return Motds.NAMES.get(random.nextInt(Motds.NAMES.size()));
    }

    public static String getFormattedMotd() {
        String[] split = wrap(getRandomMotd());
        String motd = split.length == 1 ? Motds.PREFIX + split[0] + "\n" + Motds.PREFIX : (split.length >= 2 ? Motds.PREFIX + split[0] + "\n" + Motds.SUFFIX + split[1] : Motds.PREFIX + "\n" + Motds.SUFFIX);
        return ChatColor.translateAlternateColorCodes('&', motd);
    }

    public static String[] wrap(String str) {
        return WordUtils.wrap(str, Motds.WRAP, null, true).split("\n");
    }
}
