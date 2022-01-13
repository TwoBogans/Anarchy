package org.aussie.anarchy.command.player;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.module.features.VeteranCheck;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Veteran extends Command {
    public Veteran() {
        super("veteran");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        boolean veteran;
        if (args.length >= 1) {
            OfflinePlayer player = Bukkit.getPlayer(args[0]);
            if (player == null || !player.hasPlayedBefore()) {
                ChatUtils.message(sender, Messages.PLAYER_NOT_FOUND.replace("%player%", args[0]));
                return true;
            }

            veteran = VeteranCheck.isVeteran(player);
            ChatUtils.message(sender, String.join("\n", veteran ? Messages.VETERAN_TRUE : Messages.VETERAN_FALSE).replaceAll("%player%", Objects.requireNonNull(player.getName())));
        } else if (sender instanceof Player) {
            Player player = (Player)sender;
            veteran = VeteranCheck.isVeteran(player);
            ChatUtils.message(sender, String.join("\n", veteran ? Messages.VETERAN_TRUE : Messages.VETERAN_FALSE).replaceAll("%player%", Objects.requireNonNull(player.getName())));
        }

        return false;
    }
}
