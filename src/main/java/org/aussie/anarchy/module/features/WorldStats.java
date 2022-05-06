package org.aussie.anarchy.module.features;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.aussie.anarchy.Anarchy;
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

    @Getter
    private static double size;
    @Getter
    private static int offlinePlayers;
    @Getter
    private static long age;

    private static final Calendar calender = Calendar.getInstance();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Module onEnable() {
        if (Config.WORLDSTATSTIME == -1L) {
            Config.WORLDSTATSTIME = System.currentTimeMillis();
            Config.reload();
        }

        Anarchy.getScheduler().runTaskTimer(Anarchy.getPlugin(), () -> (new Thread(() -> {
            size = calculateSize();
            offlinePlayers = Bukkit.getOfflinePlayers().length;
            age = System.currentTimeMillis() - Config.WORLDSTATSTIME;
        })).start(), 0L, Config.WORLDSTATSTICKS);
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

    public static int getYears() {
        calender.setTimeInMillis(getAge());
        return calender.get(Calendar.YEAR) - 1970;
    }

    public static int getMonths() {
        calender.setTimeInMillis(getAge());
        return calender.get(Calendar.MONTH);
    }

    public static int getDays() {
        calender.setTimeInMillis(getAge());
        return calender.get(Calendar.DATE) - 1;
    }

}
