package au.twobeetwotee.anarchy.commands.admin;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class KickAll extends Command {
    public KickAll() {
        super("kickall");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.isAdmin(sender)) {
            this.get().getScheduler().runTask(this.get(), () -> {
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
