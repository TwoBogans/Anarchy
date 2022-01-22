package org.aussie.anarchy.command.admin;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Restart extends Command {
    private int task = 0;

    public Restart() {
        super("autorestart");
        ZonedDateTime now = ZonedDateTime.now();
        Config.RESTARTTIMES.forEach((time) -> {
            int hour = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);
            int second = Integer.parseInt(time.split(":")[2]);
            ZonedDateTime next = now.withHour(hour).withMinute(minute).withSecond(second);
            if (now.compareTo(next) > 0) {
                next = next.plusDays(1L);
            }

            Duration duration = Duration.between(now, next);
            long initalDelay = duration.getSeconds();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                (new Thread(() -> {
                    this.scheduleRestart(1800);
                })).start();
            }, initalDelay, TimeUnit.DAYS.toSeconds(1L), TimeUnit.SECONDS);
        });
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.isAdmin(sender) && args.length >= 1) {
            int seconds = Integer.parseInt(args[0]);
            this.scheduleRestart(seconds);
        }

        return true;
    }

    private void scheduleRestart(int seconds) {
//        AtomicInteger secs = new AtomicInteger(seconds);
//        if (this.task != 0) {
//            this.get().getScheduler().cancelTask(this.task);
//        }
//
//        this.task = this.get().getScheduler().runTaskTimer(this.get(), () -> {
//            if (Config.RESTARTCOUNTDOWN.contains(String.valueOf(secs.get()))) {
//                this.broadcast(Messages.RESTARTING_TIME.replace("%time%", this.getTime(secs.get())));
//            }
//
//            secs.getAndDecrement();
//            if (secs.get() <= 0) {
//                this.broadcast(Messages.RESTARTING_NOW);
//                Bukkit.shutdown();
//            }
//
//        }, 0L, 20L).getTaskId();
    }

    private String getTime(int seconds) {
        int minutes = seconds * 60;
        return seconds > 60 ? minutes + " minute" + (minutes > 1 ? "s" : "") : seconds + " second" + (seconds > 1 ? "s" : "");
    }

    private void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });
        this.log(ChatColor.translateAlternateColorCodes('&', message));
    }
}
