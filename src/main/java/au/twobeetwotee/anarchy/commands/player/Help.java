package au.twobeetwotee.anarchy.commands.player;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Help extends Command {
    public Help() {
        super("help");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        ChatUtils.message(sender, String.join("\n", Messages.HELP_MESSAGE));
        return true;
    }
}
