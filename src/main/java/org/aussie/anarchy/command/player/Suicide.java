package org.aussie.anarchy.command.player;

import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Suicide extends Command {
    public Suicide() {
        super("suicide");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        if (args.length > 0 && Util.isAdmin(sender)) {
            p = Bukkit.getPlayer(args[0]);
            if (p != null) {
                p.setKiller(p);
                p.setHealth(0.0D);
                ChatUtils.message(sender, "&6You have just killed &3" + p.getName() + "!");
            }
        } else {
            if (p.isInsideVehicle() && p.getVehicle() != null) {
                p.getVehicle().eject();
            }

            p.setKiller(p);
            p.setHealth(0.0D);

        }
        return true;
    }
}
