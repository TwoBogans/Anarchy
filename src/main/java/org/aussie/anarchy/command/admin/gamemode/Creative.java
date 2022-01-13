package org.aussie.anarchy.command.admin.gamemode;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Creative extends Command {
    public Creative() {
        super("creative");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (Util.isAdmin(player)) {
                player.setGameMode(GameMode.CREATIVE);
                ChatUtils.message(player, Messages.SET_GAMEMODE.replace("%gamemode%", "creative"));
            }
        }

        return true;
    }
}