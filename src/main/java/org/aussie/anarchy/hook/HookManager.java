package org.aussie.anarchy.hook;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.LuckPermsHook;
import org.aussie.anarchy.hook.hooks.PlaceholderHook;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.hook.hooks.SparkHook;
import org.aussie.anarchy.util.hook.AbstractHookManager;

public class HookManager extends AbstractHookManager<Anarchy> {
    public HookManager(Anarchy plugin) {
        super(plugin);
        this.register("LuckPerms", LuckPermsHook.class);
        this.register("PlaceholderAPI", PlaceholderHook.class);
        this.register("ProtocolLib", ProtocolLibHook.class);
        this.register("Spark", SparkHook.class);
    }
}