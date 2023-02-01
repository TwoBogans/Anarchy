package au.twobeetwotee.anarchy.commands;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.IAnarchy;
import org.bukkit.command.CommandExecutor;

public abstract class Command implements CommandExecutor, IAnarchy {
    public String label;
    public boolean enabled = true;

    public Command(String label) {
        this.label = label;
    }

    public AnarchyPatches get() {
        return AnarchyPatches.getPlugin();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
