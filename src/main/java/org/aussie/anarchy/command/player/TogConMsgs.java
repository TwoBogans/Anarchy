package org.aussie.anarchy.command.player;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.command.Command;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class TogConMsgs extends Command implements Listener {
    private final HashMap<UUID, Boolean> toggled = new HashMap();

    public TogConMsgs() {
        super("toggleconnectionmsgs");
        Anarchy.getPlugin().registerListener(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            UUID uuid = ((Player)sender).getUniqueId();
            this.toggled.replace(uuid, !(Boolean)this.toggled.get(uuid));
            ChatUtils.message(sender, Messages.TOG_CON_MSGS.replace("%toggle%", (Boolean)this.toggled.get(uuid) ? "unhidden" : "hidden"));
        }

        return true;
    }

    @EventHandler
    private void on(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        this.toggled.putIfAbsent(e.getPlayer().getUniqueId(), false);
        ChatUtils.message(e.getPlayer(), Messages.JOIN_MOTD);
        if (!Util.isAdmin(e.getPlayer())) {

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (this.toggled.get(target.getUniqueId())) {
                    ChatUtils.sendJoinMessage(e.getPlayer(), target);
                }
            }

        }
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        this.toggled.putIfAbsent(e.getPlayer().getUniqueId(), false);
        if (!Util.isAdmin(e.getPlayer())) {

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (this.toggled.get(target.getUniqueId())) {
                    ChatUtils.message(target, Messages.QUIT_MESSAGE.replace("%player%", e.getPlayer().getName()));
                }
            }

        }
    }
}
