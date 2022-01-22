package org.aussie.anarchy.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils {
    public ChatUtils() {
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void broadcast(String message) {
        Anarchy.getPlugin().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendJoinMessage(Player player, Player target) {
        String playerName = net.md_5.bungee.api.ChatColor.stripColor(player.getDisplayName());
        ComponentBuilder builder = new ComponentBuilder(net.md_5.bungee.api.ChatColor.GRAY + playerName);
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + net.md_5.bungee.api.ChatColor.stripColor(playerName) + " "));
        builder.event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(net.md_5.bungee.api.ChatColor.GOLD + "Message " + net.md_5.bungee.api.ChatColor.DARK_AQUA + playerName)).create()));
        builder.append(" ").reset();
        builder.append(new TextComponent(net.md_5.bungee.api.ChatColor.GRAY + "joined"));
        builder.color(net.md_5.bungee.api.ChatColor.GRAY);
        target.spigot().sendMessage(builder.create());
    }
}