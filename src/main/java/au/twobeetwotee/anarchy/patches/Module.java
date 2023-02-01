package au.twobeetwotee.anarchy.patches;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.IAnarchy;
import org.bukkit.event.Listener;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Random;

public abstract class Module implements Listener, IAnarchy {
    public String name = this.getClass().getSimpleName();
    public static Random random = new Random();

    public abstract boolean isEnabled();

    public abstract Module onEnable();

    public void warn(String message) {
        AnarchyPatches.getPlugin().warn("[" + this.name + "] " + message);
    }

    public void log(String message) {
        AnarchyPatches.getPlugin().log("[" + this.name + "] " + message);
    }

    public DateTime getTime() {
        return DateTime.now(DateTimeZone.forID("Australia/Sydney"));
    }
}