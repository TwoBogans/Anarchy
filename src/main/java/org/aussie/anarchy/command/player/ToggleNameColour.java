package org.aussie.anarchy.command.player;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.module.features.TabManager;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ToggleNameColour extends Command implements Listener {

    public ToggleNameColour() {
        super("togglenamecolour");
        Anarchy.getPlugin().registerListener(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
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
