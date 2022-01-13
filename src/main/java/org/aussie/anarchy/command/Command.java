package org.aussie.anarchy.command;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.IAnarchy;
import org.bukkit.command.CommandExecutor;

public abstract class Command implements CommandExecutor, IAnarchy {
    public String label;

    public Command(String label) {
        this.label = label;
    }

    public Anarchy get() {
        return Anarchy.getPlugin();
    }
}
