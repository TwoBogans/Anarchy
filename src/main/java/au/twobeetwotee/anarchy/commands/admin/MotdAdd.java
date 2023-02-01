package au.twobeetwotee.anarchy.commands.admin;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Motds;
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
        if (Util.isAdmin(sender) && args.length >= 1) {
            String motd = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.asList(args).subList(0, args.length))).replaceAll("\\s+$", "");
            List<String> list = Motds.MOTDS;
            if (list.contains(motd)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMOTD already added!"));
                return true;
            }

            Motds.MOTDS.add(ChatColor.stripColor(motd));
            Motds.reload();
            AnarchyPatches.getPlugin().processConfigs();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully added MOTD: &6" + motd + "&r, &a" + Motds.MOTDS.size() + " MOTDs loaded."));
        }

        return true;
    }
}
