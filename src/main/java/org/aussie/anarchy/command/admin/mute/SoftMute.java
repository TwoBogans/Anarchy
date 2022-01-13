package org.aussie.anarchy.command.admin.mute;


import org.aussie.anarchy.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

// TODO SOFT MUTE COMMAND (TIME LIMIT MUTE)
public class SoftMute extends Command {
    public SoftMute() {
        super("softmute");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}