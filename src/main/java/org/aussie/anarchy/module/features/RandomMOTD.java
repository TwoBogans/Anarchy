package org.aussie.anarchy.module.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.apache.commons.lang.WordUtils;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Motds;
import org.aussie.anarchy.util.packet.wrappers.WrapperHandshakingClientSetProtocol;
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

    private final Random random = new Random();
    private final HashMap<String, Boolean> domainMap = new HashMap<>();

    public RandomMOTD() {
    }

    public boolean isEnabled() {
        return Motds.ENABLED;
    }

    public Module onEnable() {
        get().getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(get(), ListenerPriority.HIGHEST, PacketType.Handshake.Client.SET_PROTOCOL) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                final WrapperHandshakingClientSetProtocol wrappedPacket = new WrapperHandshakingClientSetProtocol(packet);

                if(wrappedPacket.getServerAddressHostnameOrIp().toLowerCase().contains("aussie")) {
                    domainMap.put(Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().toString(), true);
                } else {
                    domainMap.put(Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().toString(), false);
                }
            }
        });
        return this;
    }

    @EventHandler(
        priority = EventPriority.HIGH
    )
    private void on(ServerListPingEvent event) {
        event.setMotd(this.getFormattedMotd());

        try {
            String name = domainMap.get(event.getAddress().toString()) ? "aussieanarchy" : "2b2tau";
            File file = new File(get().getDataFolder(), "icons/" + name + ".png");

            event.setServerIcon(get().getServer().loadServerIcon(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRandomMotd() {
        return Motds.MOTDS.get(this.random.nextInt(Motds.MOTDS.size())).replaceAll("%name%", this.getRandomName());
    }

    private String getRandomName() {
        return Motds.NAMES.get(this.random.nextInt(Motds.NAMES.size()));
    }

    private String getFormattedMotd() {
        String[] split = this.wrap(this.getRandomMotd());
        String motd = split.length == 1 ? Motds.PREFIX + split[0] + "\n" + Motds.PREFIX : (split.length >= 2 ? Motds.PREFIX + split[0] + "\n" + Motds.SUFFIX + split[1] : Motds.PREFIX + "\n" + Motds.SUFFIX);
        return ChatColor.translateAlternateColorCodes('&', motd);
    }

    private String[] wrap(String str) {
        return WordUtils.wrap(str, Motds.WRAP, null, true).split("\n");
    }
}
