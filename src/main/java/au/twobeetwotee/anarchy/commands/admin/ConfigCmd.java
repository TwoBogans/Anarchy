package au.twobeetwotee.anarchy.commands.admin;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Config;
import au.twobeetwotee.anarchy.utils.config.Messages;
import au.twobeetwotee.anarchy.utils.config.Motds;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ConfigCmd extends Command {
    public ConfigCmd() {
        super("config");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (!Util.isAdmin(sender)) {
            return false;
        } else if (args.length >= 1) {
            String var5 = args[0];
            byte var6 = -1;
            switch(var5.hashCode()) {
            case -934641255:
                if (var5.equals("reload")) {
                    var6 = 0;
                }
                break;
            case 3198785:
                if (var5.equals("help")) {
                    var6 = 1;
                }
            }

            switch(var6) {
            case 0:
                if (args.length >= 2) {
                    String var7 = args[1];
                    byte var8 = -1;
                    switch(var7.hashCode()) {
                    case -1354792126:
                        if (var7.equals("config")) {
                            var8 = 1;
                        }
                        break;
                    case 96673:
                        if (var7.equals("all")) {
                            var8 = 0;
                        }
                        break;
                    case 108417:
                        if (var7.equals("msg")) {
                            var8 = 3;
                        }
                        break;
                    case 104085281:
                        if (var7.equals("motds")) {
                            var8 = 4;
                        }
                        break;
                    case 954925063:
                        if (var7.equals("message")) {
                            var8 = 2;
                        }
                    }

                    switch (var8) {
                        case 0 -> {
                            Config.reloadAll();
                            ChatUtils.message(sender, String.join("\n", Messages.CONFIG_RELOADED).replaceAll("%config%", args[1]));
                            return true;
                        }
                        case 1 -> {
                            Config.reload();
                            ChatUtils.message(sender, String.join("\n", Messages.CONFIG_RELOADED).replaceAll("%config%", args[1]));
                            return true;
                        }
                        case 2, 3 -> {
                            Messages.reload();
                            ChatUtils.message(sender, String.join("\n", Messages.CONFIG_RELOADED).replaceAll("%config%", args[1]));
                            return true;
                        }
                        case 4 -> {
                            Motds.reload();
                            ChatUtils.message(sender, String.join("\n", Messages.CONFIG_RELOADED).replaceAll("%config%", args[1]));
                            return true;
                        }
                        default -> {
                            ChatUtils.message(sender, String.join("\n", Messages.CONFIG_HELP));
                            return true;
                        }
                    }
                } else {
                    ChatUtils.message(sender, String.join("\n", Messages.CONFIG_HELP));
                }
            case 1:
                ChatUtils.message(sender, String.join("\n", Messages.CONFIG_HELP));
            }

            return true;
        } else {
            ChatUtils.message(sender, String.join("\n", Messages.CONFIG_HELP));
            return false;
        }
    }
}
