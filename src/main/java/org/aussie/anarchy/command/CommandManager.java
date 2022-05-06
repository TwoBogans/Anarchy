package org.aussie.anarchy.command;

import org.aussie.anarchy.command.admin.gamemode.*;
import org.aussie.anarchy.command.player.*;
import org.aussie.anarchy.command.admin.*;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.ChatUtils;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandManager implements Listener {
    public CommandManager(Anarchy plugin) {
        plugin.registerListener(this);
        this.registerCommands(
                new Creative(),
                new Spectator(),
                new Survival(),
                new ConfigCmd(),
                new EnderChest(),
                new InvSee(),
                new KickAll(),
                new MotdAdd(),
                new Queue(),
//                new Restart(),
                new ServerSay(),
                new SudoAll(),
                new Help(),
                new Jihad(),
                new Playtime(),
                new Stats(),
                new Suicide(),
                new TogConMsgs(),
                new Veteran());
    }

    private void registerCommands(Command... commands) {
        AtomicInteger i = new AtomicInteger();
        Command[] var3 = commands;
        int var4 = commands.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Command command = var3[var5];
            PluginCommand pc = Anarchy.getPlugin().getCommand(command.label);
            if (pc != null) {
                pc.setExecutor(command);
                i.getAndIncrement();
            }
        }

        Anarchy.getPlugin().log(ChatColor.DARK_GREEN + "[Commands] Registered " + i.get() + " commands!");
    }

    @EventHandler(
        priority = EventPriority.HIGH
    )
    private void on(PlayerCommandPreprocessEvent e) {
        if (!Util.isAdmin(e.getPlayer())) {
            if (!Config.COMMANDSWHITELISTED.contains(e.getMessage().split(" ")[0].toLowerCase())) {
                ChatUtils.message(e.getPlayer(), Messages.BAD_COMMAND);
                e.setCancelled(true);
            }

        }
    }
}