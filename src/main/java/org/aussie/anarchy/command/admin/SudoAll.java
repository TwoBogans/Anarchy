package org.aussie.anarchy.command.admin;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SudoAll extends Command {
    public SudoAll() {
        super("sudoall");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.isAdmin(sender) && args.length > 0) {
            Bukkit.getOnlinePlayers().forEach((player) -> {
                StringBuilder input = new StringBuilder();
                String[] var3 = args;
                int var4 = args.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String arg = var3[var5];
                    input.append(arg).append(" ");
                }

                input = new StringBuilder(input.toString().trim());
                boolean cmd = input.toString().startsWith("/");
                if (cmd) {
                    input = new StringBuilder(input.substring(1));
                    player.performCommand(input.toString());
                } else {
                    player.chat(input.toString());
                }

            });
        }

        return true;
    }
}