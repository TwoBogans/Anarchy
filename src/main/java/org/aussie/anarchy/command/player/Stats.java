package org.aussie.anarchy.command.player;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.module.features.WorldStats;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Stats extends Command {
    public Stats() {
        super("worldstats");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Iterator var5 = Messages.WORLDSTATS.iterator();

        while(var5.hasNext()) {
            String msg = (String)var5.next();
            sender.sendMessage(WorldStats.parse(sender, msg));
        }

        return false;
    }
}
