package org.aussie.anarchy.command.player;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.config.Messages;
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
