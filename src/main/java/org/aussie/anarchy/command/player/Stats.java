package org.aussie.anarchy.command.player;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.module.features.WorldStats;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Stats extends Command {
    public Stats() {
        super("worldstats");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        ChatUtils.message(sender, WorldStats.parse(sender, String.join("\n", Messages.WORLDSTATS)));

//        for (String msg : Messages.WORLDSTATS) {
//            sender.sendMessage(WorldStats.parse(sender, msg));
//        }

        return false;
    }
}
