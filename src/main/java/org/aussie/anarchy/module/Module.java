package org.aussie.anarchy.module;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.IAnarchy;
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
        Anarchy.getPlugin().warn("[" + this.name + "] " + message);
    }

    public void log(String message) {
        Anarchy.getPlugin().log("[" + this.name + "] " + message);
    }

    public DateTime getTime() {
        return DateTime.now(DateTimeZone.forID("Australia/Sydney"));
    }
}