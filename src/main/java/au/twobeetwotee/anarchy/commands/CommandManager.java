package au.twobeetwotee.anarchy.commands;

import au.twobeetwotee.anarchy.commands.player.*;
import au.twobeetwotee.anarchy.commands.admin.*;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.ChatUtils;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Config;
import au.twobeetwotee.anarchy.utils.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandManager implements Listener {
    public CommandManager(AnarchyPatches plugin) {
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
            if (!command.enabled) continue;
            PluginCommand pc = AnarchyPatches.getPlugin().getCommand(command.label);
            if (pc != null) {
                pc.setExecutor(command);
                i.getAndIncrement();
            }
        }

        AnarchyPatches.getPlugin().log(ChatColor.DARK_GREEN + "[Commands] Registered " + i.get() + " commands!");
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