package org.aussie.anarchy.command.admin;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Motds;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MotdAdd extends Command {
    public MotdAdd() {
        super("motdadd");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.isAdmin(sender) && args.length > 1) {
            String motd = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.asList(args).subList(1, args.length))).replaceAll("\\s+$", "");
            List<String> list = Motds.MOTDS;
            if (list.contains(motd)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMOTD already added!"));
                return true;
            }

            Motds.MOTDS.add(ChatColor.stripColor(motd));
            Motds.reload();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully added MOTD: &6" + motd + "&r, &a" + Motds.MOTDS.size() + " MOTDs loaded."));
        }

        return true;
    }
}
