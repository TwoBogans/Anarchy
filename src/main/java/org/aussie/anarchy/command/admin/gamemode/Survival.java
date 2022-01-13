package org.aussie.anarchy.command.admin.gamemode;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Survival extends Command {
    public Survival() {
        super("survival");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (Util.isAdmin(player)) {
                player.setGameMode(GameMode.SURVIVAL);
                ChatUtils.message(player, Messages.SET_GAMEMODE.replace("%gamemode%", "survival"));
            }
        }

        return true;
    }
}