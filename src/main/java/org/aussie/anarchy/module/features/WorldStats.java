package org.aussie.anarchy.module.features;

import me.clip.placeholderapi.PlaceholderAPI;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

public class WorldStats extends Module {
    private static double size;
    private static int offlinePlayers;
    private static long age;
    private static final Calendar calendar = Calendar.getInstance();

    public WorldStats() {
    }

    public boolean isEnabled() {
        return true;
    }

    public Module onEnable() {
        if (Config.WORLDSTATSTIME == -1L) {
            Config.WORLDSTATSTIME = System.currentTimeMillis();
            Config.reload();
        }

        get().getScheduler().runTaskTimer(get(), () -> {
            (new Thread(() -> {
                size = this.calculateSize();
                offlinePlayers = Bukkit.getOfflinePlayers().length;
                age = System.currentTimeMillis() - Config.WORLDSTATSTIME;
            })).start();
        }, 0L, (long)Config.WORLDSTATSTICKS);
        return this;
    }

    private double calculateSize() {
        double l = 0.0D;

        for (String s : Config.WORLDSTATSFILE) {
            File[] var5 = Objects.requireNonNull((new File(s)).listFiles());

            for (File file : var5) {
                if (file.isFile()) {
                    l += (double) file.length();
                }
            }
        }

        double giga = l / 1048576.0D / 1000.0D;
        double tera = giga / 1000.0D;
        return Config.WORLDSTATSINTB ? tera : giga;
    }

    public static String parse(@NotNull CommandSender sender, String msg) {
        msg = msg.replace("%years%", String.valueOf(getYears()));
        msg = msg.replace("%months%", String.valueOf(getMonths()));
        msg = msg.replace("%days%", String.valueOf(getDays()));
        msg = msg.replace("%size%", (new DecimalFormat("#.##")).format(getSize()));
        msg = msg.replace("%players%", String.valueOf(getOfflinePlayers()));
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Player player = sender instanceof Player ? (Player)sender : null;
            msg = PlaceholderAPI.setPlaceholders(player, msg);
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private static int getYears() {
        calendar.setTimeInMillis(getAge());
        return calendar.get(1) - 1970;
    }

    private static int getMonths() {
        calendar.setTimeInMillis(getAge());
        return calendar.get(2);
    }

    private static int getDays() {
        calendar.setTimeInMillis(getAge());
        return calendar.get(Calendar.DATE) - 1;
    }

    public static double getSize() {
        return size;
    }

    public static int getOfflinePlayers() {
        return offlinePlayers;
    }

    public static long getAge() {
        return age;
    }
}
