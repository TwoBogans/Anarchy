package au.twobeetwotee.anarchy.commands.player;

import au.twobeetwotee.anarchy.commands.Command;
import au.twobeetwotee.anarchy.patches.modules.Anniversary;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Jihad extends Command {

    public Jihad() {
        super("jihad");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Anniversary.onCommand(sender);
    }
}
