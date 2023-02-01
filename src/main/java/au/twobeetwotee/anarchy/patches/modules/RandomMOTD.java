package au.twobeetwotee.anarchy.patches.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.apache.commons.lang.WordUtils;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Motds;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.HashMap;

public class RandomMOTD extends Module {

    private final HashMap<String, Boolean> domainMap = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Motds.ENABLED;
    }

    public Module onEnable() {
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.HIGHEST, PacketType.Handshake.Client.SET_PROTOCOL) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
//                PacketContainer packet = event.getPacket();
//
//                final WrapperHandshakingClientSetProtocol wrappedPacket = new WrapperHandshakingClientSetProtocol(packet);
//                final String domain = wrappedPacket.getServerAddressHostnameOrIp().toLowerCase();
//
//                System.out.printf("[DEBUG] [RandomMOTD] Domain: %s", domain);
//
//                if(domain.contains("aussie")) {
//                    domainMap.put(domain, true);
//                } else {
//                    domainMap.put(domain, false);
//                }
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
        return ChatColor.translateAlternateColorCodes('&',
                split.length == 1 ? Motds.PREFIX + split[0] + "\n" + Motds.PREFIX : (
                split.length >= 2 ? Motds.PREFIX + split[0] + "\n" + Motds.SUFFIX + split[1] : Motds.PREFIX + "\n" + Motds.SUFFIX));
    }

    public static String[] wrap(String str) {
        return WordUtils.wrap(str, Motds.WRAP, null, true).split("\n");
    }
}
