package org.aussie.anarchy.module;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.IAnarchy;
import org.bukkit.event.Listener;

public abstract class Module implements Listener, IAnarchy {
    public String name = this.getClass().getSimpleName();

    public Module() {
    }

    public abstract boolean isEnabled();

    public abstract Module onEnable();

    public static Anarchy get() {
        return Anarchy.getPlugin();
    }

    public void warn(String message) {
        get().warn("[" + this.name + "] " + message);
    }

    public void log(String message) {
        get().log("[" + this.name + "] " + message);
    }
}