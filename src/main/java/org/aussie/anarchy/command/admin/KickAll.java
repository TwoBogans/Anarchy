package org.aussie.anarchy.command.admin;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class KickAll extends Command {
    public KickAll() {
        super("kickall");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.isAdmin(sender)) {
            this.get().getScheduler().runTaskAsynchronously(this.get(), () -> {
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    if (p != sender) {
                        p.kickPlayer(Messages.KICK_MESSAGE);
                    }

                });
            });
        }

        return true;
    }
}
