package au.twobeetwotee.anarchy.commands.admin;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ServerSay extends Command {
    public ServerSay() {
        super("say");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.isAdmin(sender)) {
            if (args.length > 0) {
                ChatUtils.broadcast("&e[SERVER] " + Arrays.toString(args).replace(",", "").replace("[", "").replace("]", ""));
            } else {
                ChatUtils.message(sender, Messages.USAGE_COMMAND.replace("%syntax%", "/say <message>"));
            }
        }

        return true;
    }
}
