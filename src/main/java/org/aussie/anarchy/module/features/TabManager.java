package org.aussie.anarchy.module.features;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.PlaceholderHook;
import org.aussie.anarchy.hook.hooks.SparkHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TabManager extends Module implements Runnable {
    public TabManager() {
    }

    public boolean isEnabled() {
        return Config.TABENABLED;
    }

    public Module onEnable() {
        get().getScheduler().runTaskTimer(get(), new TabManager(), 0L, 10L);
        get().getScheduler().scheduleSyncRepeatingTask(get(), this, 0L, 20L);
        return this;
    }

    public void run() {
        if (Bukkit.getOnlinePlayers().size() != 0) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                StringBuilder header = new StringBuilder();
                StringBuilder footer = new StringBuilder();
                List<String> headerList, footerList;
                if (Util.containsDomain(player, Messages.TAB1DOMAIN, "aussieanarchy.org")) {
                    headerList = Messages.HEADER1.stream().map((str) -> this.parseText(player, str)).collect(Collectors.toList());
                    footerList = Messages.FOOTER1.stream().map((str) -> this.parseText(player, str)).collect(Collectors.toList());
                } else {
                    headerList = Messages.HEADER.stream().map((str) -> this.parseText(player, str)).collect(Collectors.toList());
                    footerList = Messages.FOOTER.stream().map((str) -> this.parseText(player, str)).collect(Collectors.toList());
                }

                int i;
                for (i = 0; i < headerList.size(); ++i) {
                    header.append(headerList.get(i)).append(i == footerList.size() - 1 ? "" : "\n");
                }

                for (i = 0; i < footerList.size(); ++i) {
                    footer.append(footerList.get(i)).append(i == footerList.size() - 1 ? "" : "\n");
                }

                player.setPlayerListHeaderFooter((new ComponentBuilder(header.toString())).create(), (new ComponentBuilder(footer.toString())).create());
            }

        }
    }

    private String parseText(Player player, String text) {
        String newText = text;
        if (get().getHookManager().getHook(PlaceholderHook.class).isPapiEnabled()) {
            newText = PlaceholderAPI.setPlaceholders(player, text);
        }

        newText = ChatColor.translateAlternateColorCodes('&', newText);
        newText = newText.replaceAll("%tps%", this.getTps().toLegacyText()).replaceAll("%ping%", this.formattedPing(player, false)).replaceAll("%uptime%", this.getFormattedInterval(System.currentTimeMillis() - get().getStartMillis())).replaceAll("%players%", Integer.toString(this.getPlayerCount()));
        return newText;
    }

    private TextComponent formatTps(double tps) {
        net.md_5.bungee.api.ChatColor color = tps > 16.0D ? net.md_5.bungee.api.ChatColor.GREEN : (tps > 10.0D ? net.md_5.bungee.api.ChatColor.YELLOW : net.md_5.bungee.api.ChatColor.RED);
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
        return this.formatTps(Anarchy.getPlugin().getHookManager().getHook(SparkHook.class).getTPS_10SEC());
    }

    private String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    private String formattedPing(Player p, boolean color) {
        return color ? (this.ping(p) < 300.0D ? net.md_5.bungee.api.ChatColor.GREEN : (this.ping(p) > 800.0D ? net.md_5.bungee.api.ChatColor.RED : net.md_5.bungee.api.ChatColor.GOLD)) + "" + this.format(this.ping(p), 2) : this.format(this.ping(p), 2);
    }

    private double ping(Player player) {
        if (!CompatUtil.is1_13()) {
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Integer ping = (Integer)entityPlayer.getClass().getField("ping").get(entityPlayer);
                return ping.doubleValue();
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException var4) {
                return -1.0D;
            }
        } else {
            return (double)player.getPing();
        }
    }

    private String format(double i, int p) {
        String form = "#";
        if (p > 0) {
            form = form + "." + this.repeat("#", p);
        }

        return (new DecimalFormat(form)).format(i);
    }

    private String repeat(String s, int n) {
        if (s == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < n; ++i) {
                sb.append(s);
            }

            return sb.toString();
        }
    }
}
