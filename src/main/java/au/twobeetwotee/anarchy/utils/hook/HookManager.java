package au.twobeetwotee.anarchy.utils.hook;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.LuckPermsHook;
import au.twobeetwotee.anarchy.utils.hook.hooks.PlaceholderHook;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import au.twobeetwotee.anarchy.utils.hook.hooks.SparkHook;

public class HookManager extends AbstractHookManager<AnarchyPatches> {
    public HookManager(AnarchyPatches plugin) {
        super(plugin);
        this.register("LuckPerms", LuckPermsHook.class);
        this.register("PlaceholderAPI", PlaceholderHook.class);
        this.register("ProtocolLib", ProtocolLibHook.class);
        this.register("Spark", SparkHook.class);
    }
}