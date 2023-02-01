package au.twobeetwotee.anarchy.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import au.twobeetwotee.anarchy.AnarchyPatches;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.Normalizer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ChatUtils {

    private static final Pattern nonASCII = Pattern.compile("[^\\x00-\\x7f]");

    public ChatUtils() {
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void broadcast(String message) {
        AnarchyPatches.getPlugin().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
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

    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static String removeNonASCII(String string) {
        return nonASCII.matcher(string).replaceAll("");
    }
}