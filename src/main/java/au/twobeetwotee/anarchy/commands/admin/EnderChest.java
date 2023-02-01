package au.twobeetwotee.anarchy.commands.admin;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderChest extends Command {
    public EnderChest() {
        super("enderchest");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (Util.isAdmin(player)) {
                if (args.length != 1) {
                    ChatUtils.message(player, Messages.USAGE_COMMAND.replace("%syntax%", "/ec <player>"));
                    return false;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    ChatUtils.message(player, Messages.PLAYER_NOT_ONLINE.replace("%player%", args[0]));
                    return false;
                }

                player.openInventory(target.getEnderChest());
            }
        }

        return true;
    }
}
