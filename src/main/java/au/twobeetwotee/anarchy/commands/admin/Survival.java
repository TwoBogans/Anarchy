package au.twobeetwotee.anarchy.commands.admin;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Survival extends Command {
    public Survival() {
        super("survival");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (Util.isAdmin(player)) {
                player.setGameMode(GameMode.SURVIVAL);
                ChatUtils.message(player, Messages.SET_GAMEMODE.replace("%gamemode%", "survival"));
            }
        }

        return true;
    }
}