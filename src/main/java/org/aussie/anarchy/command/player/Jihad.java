package org.aussie.anarchy.command.player;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.module.seasonal.Anniversary;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Jihad extends Command {

    public Jihad() {
        super("jihad");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Anniversary.onCommand(sender);
    }
}
