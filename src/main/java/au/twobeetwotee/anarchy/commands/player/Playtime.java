package au.twobeetwotee.anarchy.commands.player;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.compat.CompatUtil;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Playtime extends Command {
    public Playtime() {
        super("playtime");
    }

    // TODO FIX when using /jd <playername> it will bring up your own jd as well
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                this.tryOfflineMessage(player, args);
                return true;
            }

            this.sendPlaytimeMessage(player, null);
        }

        if (sender instanceof ConsoleCommandSender && args.length > 0) {
            this.tryOfflineMessage(sender, args);
        }

        return true;
    }

    private void tryOfflineMessage(@NotNull CommandSender sender, @NotNull String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            try {
                this.sendOfflineMessage(sender, Bukkit.getOfflinePlayer(args[0]));
            } catch (Exception var5) {
                ChatUtils.message(sender, Messages.PLAYER_NOT_FOUND.replace("%player%", args[0]));
            }

        } else {
            this.sendPlaytimeMessage(sender, target);
        }
    }

    private void sendOfflineMessage(CommandSender sender, OfflinePlayer target) {
        if (target == null) {
            throw new IllegalArgumentException();
        } else {
            if (target.hasPlayedBefore()) {
                long firstPlayed = target.getFirstPlayed();
                long lastPlayed = target.getLastPlayed();
                ChatUtils.message(sender, String.join("\n", Messages.PLAYTIME_OFFLINE).replace("%player%", Objects.requireNonNull(target.getName())).replace("%firstplayed%", this.getFormattedDate(firstPlayed)).replace("%lastplayed%", this.getFormattedDate(lastPlayed)));
            } else {
                ChatUtils.message(sender, Messages.PLAYER_NOT_FOUND.replace("%player%", Objects.requireNonNull(target.getName())));
            }

        }
    }

    private void sendPlaytimeMessage(CommandSender sender, Player target) {
        Statistic statistic = CompatUtil.is1_12() ? Statistic.valueOf("PLAY_ONE_TICK") : Statistic.PLAY_ONE_MINUTE;
        if (target == null) {
            if (sender instanceof Player player) {
                ChatUtils.message(sender, String.join("\n", Messages.PLAYTIME_ONLINE).replace("%player%", player.getName()).replace("%firstplayed%", this.getFormattedDate(player.getFirstPlayed())).replace("%playtime%", this.getFormattedTime(player.getStatistic(statistic))));
            }

        } else {
            ChatUtils.message(sender, String.join("\n", Messages.PLAYTIME_ONLINE).replace("%player%", target.getDisplayName()).replace("%firstplayed%", this.getFormattedDate(target.getFirstPlayed())).replace("%playtime%", this.getFormattedTime(target.getStatistic(statistic))));
        }
    }

    private String getFormattedDate(long l) {
        Date date = new Date(l);
        return "&a" + (new SimpleDateFormat("E, MMM dd yyyy")).format(date);
    }

    private String getFormattedTime(int i) {
        int ticks = i / 20;
        int days = (int) TimeUnit.SECONDS.toDays(ticks);
        int hours = (int)(TimeUnit.SECONDS.toHours(ticks) - TimeUnit.DAYS.toHours(days));
        int minutes = (int)(TimeUnit.SECONDS.toMinutes(ticks) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days));
        int seconds = (int)(TimeUnit.SECONDS.toSeconds(ticks) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days));
        if (days != 0) {
            return "&a" + days + "&3 days, &a" + hours + "&3 hours, &a" + minutes + "&3 mins, &a" + seconds + "&3 secs";
        } else if (hours != 0) {
            return "&a" + hours + "&3 hours, &a" + minutes + "&3 minutes, &a" + seconds + "&3 seconds";
        } else {
            return minutes != 0 ? "&a" + minutes + "&3 minutes, &a" + seconds + "&3 seconds" : "&a" + seconds + "&3 seconds";
        }
    }
}