package au.twobeetwotee.anarchy.commands.player;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.patches.modules.TabManager;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ToggleNameColour extends Command implements Listener {

    public ToggleNameColour() {
        super("togglenamecolour");
        AnarchyPatches.getPlugin().registerListener(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();

            TabManager.getToggled().replace(uuid, !TabManager.getToggled().get(uuid));

            ChatUtils.message(sender, Messages.TOGGLE_NAME_COLOUR.replace("%toggle%", TabManager.getToggled().get(uuid) ? "unhidden" : "hidden"));
        }
        return true;
    }

    @EventHandler
    private void on(PlayerJoinEvent e) {
        putIfAbsentForOnlinePlayers();
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        putIfAbsentForOnlinePlayers();
    }

    private void putIfAbsentForOnlinePlayers() {
        for (Player target : Bukkit.getOnlinePlayers()) {
            TabManager.getToggled().putIfAbsent(target.getUniqueId(), false);
        }
    }

}
