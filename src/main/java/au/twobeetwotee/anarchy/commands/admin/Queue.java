package au.twobeetwotee.anarchy.commands.admin;

import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.model.user.User;
import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.utils.hook.hooks.LuckPermsHook;
import au.twobeetwotee.anarchy.utils.hook.hooks.PlaceholderHook;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Queue extends Command {
    public Queue() {
        super("queue");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (Util.isAdmin(player)) {
                int regular = 0;
                int priority = 0;
                int veteran = 0;
                Iterator var9 = Bukkit.getOnlinePlayers().iterator();

                while(var9.hasNext()) {
                    Player online = (Player)var9.next();
                    LuckPermsHook perms = this.get().getHookManager().getHook(LuckPermsHook.class);
                    User user = perms.getUser(online.getUniqueId());
                    if (user != null) {
                        if (perms.hasPermission(user, "group.veteran")) {
                            ++veteran;
                        } else if (perms.hasPermission(user, "group.priority")) {
                            ++priority;
                        } else if (perms.hasPermission(user, "group.default")) {
                            ++regular;
                        }
                    }
                }

                boolean papi = this.get().getHookManager().getHook(PlaceholderHook.class).isPapiEnabled();
                ChatUtils.message(sender, String.join("\n", Messages.QUEUE_MESSAGE).replace("%regular_online%", String.valueOf(regular)).replace("%priority_online%", String.valueOf(priority)).replace("%veteran_online%", String.valueOf(veteran)).replace("%regular%", papi ? PlaceholderAPI.setPlaceholders(null, "%pistonqueue_regular%") : "0").replace("%priority%", papi ? PlaceholderAPI.setPlaceholders(null, "%pistonqueue_priority%") : "0").replace("%veteran%", papi ? PlaceholderAPI.setPlaceholders(null, "%pistonqueue_veteran%") : "0"));
            }
        }

        return true;
    }
}
