package org.aussie.anarchy.module.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import lombok.Getter;
import lombok.var;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.PlaceholderHook;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.hook.hooks.SparkHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.config.Messages;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayServerPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TabManager extends Module {

    @Getter
    private static final HashMap<UUID, Boolean> toggled = new HashMap<>();

    public boolean isEnabled() {
        return Config.TABENABLED;
    }

    public Module onEnable() {
        TabList tabList = new TabList(this);

        Anarchy.getScheduler().runTaskTimer(
                Anarchy.getPlugin(),
                tabList,
                0L,
                Config.TABUPDATEDELAY);

        Anarchy.getHookManager()
                .getHook(ProtocolLibHook.class)
                .add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.PLAYER_INFO)
        {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event.getPacket());

                List<PlayerInfoData> newPlayers = new ArrayList<>();

                boolean displayColour = toggled.get(event.getPlayer().getUniqueId());

                packet.getData().forEach(data -> {
                    var clone = data.getDisplayName();
                    var plain = WrappedChatComponent.fromJson(ChatColor.stripColor(clone.getJson()));
                    var color = WrappedChatComponent.fromLegacyText(getNameColorFromProfile(data.getProfile()));
                    var newData = new PlayerInfoData(
                            data.getProfile(),
                            data.getLatency(),
                            data.getGameMode(),
                            (displayColour) ? color : plain
                    );

                    newPlayers.add(newData);
                });

                packet.setData(newPlayers);
            }
        });

        return this;
    }


    private String getNameColorFromProfile(WrappedGameProfile profile) {
        // TODO Name Colour
        return profile.getName();
    }

    public static class TabList implements Runnable {
        final TabManager tab;

        public TabList(TabManager tab) {
            this.tab = tab;
        }

        public void run() {
            try {
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    StringBuilder header = new StringBuilder();
                    StringBuilder footer = new StringBuilder();

                    List<String> headerList = Messages.TAB_HEADER.stream().map((str) -> tab.parseText(player, str)).collect(Collectors.toList());
                    List<String> footerList = Messages.TAB_FOOTER.stream().map((str) -> tab.parseText(player, str)).collect(Collectors.toList());

                    for (int i = 0; i < headerList.size(); ++i) {
                        header.append(headerList.get(i)).append(i == footerList.size() - 1 ? "" : "\n");
                    }

                    for (int i = 0; i < footerList.size(); ++i) {
                        footer.append(footerList.get(i)).append(i == footerList.size() - 1 ? "" : "\n");
                    }

                    player.setPlayerListHeaderFooter(new ComponentBuilder(header.toString()).create(), new ComponentBuilder(footer.toString()).create());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String parseText(Player player, String text) {
        String newText = text;

        if (Anarchy.getHookManager().getHook(PlaceholderHook.class).isPapiEnabled()) {
            newText = PlaceholderAPI.setPlaceholders(player, text);
        }

        newText = ChatColor.translateAlternateColorCodes('&', newText);
        newText = newText
                .replaceAll("%tps%", this.getTps().toLegacyText())
                .replaceAll("%ping%", this.formattedPing(player, Config.TABCOLORPING))
                .replaceAll("%uptime%", this.getFormattedInterval(System.currentTimeMillis() - Anarchy.getStartMillis()))
                .replaceAll("%players%", Integer.toString(this.getPlayerCount()));

        return newText;
    }

    private TextComponent formatTps(double tps) {
        net.md_5.bungee.api.ChatColor color = tps > 16.0D
                ? net.md_5.bungee.api.ChatColor.GREEN : (tps > 10.0D
                ? net.md_5.bungee.api.ChatColor.YELLOW : net.md_5.bungee.api.ChatColor.RED);

        double roundedTps = Math.min((double)Math.round(tps * 100.0D) / 100.0D, 20.0D);

        String tpsString = (tps > 19.95D ? "*" : "") + (roundedTps > 19.8D ? 20.0D : roundedTps);
        TextComponent text = new TextComponent(tpsString);
        text.setColor(color);

        return text;
    }

    private int getPlayerCount() {
        int i = 0;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!this.isVanished(p)) {
                ++i;
            }
        }

        return i;
    }

    private boolean isVanished(Player player) {
        Iterator<MetadataValue> var2 = player.getMetadata("vanished").iterator();

        MetadataValue meta;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            meta = var2.next();
        } while(!meta.asBoolean());

        return true;
    }

    private TextComponent getTps() {
        return this.formatTps(Anarchy.getHookManager().getHook(SparkHook.class).getTPS_10SEC());
    }

    private String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    private String formattedPing(Player p, boolean color) {
        double ping = this.ping(p);

        if (ping >= 1000) {
            double seconds = ping / 1000.0D;
            return (color ? ChatColor.RED + "" : "") + format(seconds) + "s";
        }

        return (color ? (ping < 300.0D ? net.md_5.bungee.api.ChatColor.GREEN : (ping > 800.0D ? net.md_5.bungee.api.ChatColor.RED : net.md_5.bungee.api.ChatColor.GOLD)) + "" + this.format(ping) : this.format(ping)) + "ms";
    }

    private double ping(Player player) {
        if (!CompatUtil.is1_13()) {
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Integer ping = (Integer) entityPlayer.getClass().getField("ping").get(entityPlayer);
                return ping.doubleValue();
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException var4) {
                return -1.0D;
            }
        } else {
            return player.getPing();
        }
    }

    private String format(double i) {
        return (new DecimalFormat("#.#")).format(i);
    }
}
