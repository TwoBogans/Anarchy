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
        String playerName = ChatColor.stripColor(player.getDisplayName());
        ComponentBuilder name = new ComponentBuilder(playerName);
        name.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Messages.JOIN_CLICK.replace("%player%", playerName)));
        name.event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(Messages.JOIN_HOVER.replace("%player%", playerName))).create()));
        ComponentBuilder builder = new ComponentBuilder(Messages.JOIN_MESSAGE.replace("%player%", (new TextComponent(name.create())).toLegacyText()));
        target.spigot().sendMessage(builder.create());
    }
}