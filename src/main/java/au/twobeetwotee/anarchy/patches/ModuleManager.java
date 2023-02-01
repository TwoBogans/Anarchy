package au.twobeetwotee.anarchy.patches;

import au.twobeetwotee.anarchy.patches.modules.*;
import lombok.Getter;
import au.twobeetwotee.anarchy.AnarchyPatches;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleManager {
    @Getter
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager(AnarchyPatches plugin) {
        this.addModules(
                // Features
                new Chat(),
                new Miscellaneous(),
//                new NetherCactusDupe(),
                new NoGlobalThunder(),
                new RandomMOTD(),
                new RandomSpawn(),
                new TabManager(),
                new VeteranCheck(),
                new WitherSound(),
                // Patches
                new AntiBedrockHoles(),
                new AntiBoatFly(),
                new AntiBurrow(),
                new AntiChunkBan(),
                new AntiCoordExploit(),
                new AntiDupe(),
                new AntiElytra(),
                new AntiExploits(),
                new AntiGodMode(),
                new AntiIllegals(),
                new AntiInstantMine(),
                new AntiLag(),
                new AntiNetherRoof(),
                new AntiLiquidLag(),
                new AntiPacketFly(),
                new AntiProjectileVelocity(),
                new AntiRedstone(),
                new AntiWitherLag(),
                // Seasonal
                new Anniversary(),
                new Halloween()
        );
        AtomicInteger i = new AtomicInteger();
        this.modules.forEach((p) -> {
            if (p.isEnabled()) {
                plugin.registerListener(p.onEnable());
                i.getAndIncrement();
            }
        });
        plugin.log(ChatColor.DARK_GREEN + "[Features] Enabled " + i.get() + " patches/features");
    }

    private void addModules(Module... modules) {
        this.modules.addAll(Arrays.asList(modules));
    }

    public static Module getModuleByClass(Class<?> clazz) {
        Iterator<Module> var2 = AnarchyPatches.getModuleManager().modules.iterator();

        Module module;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            module = var2.next();
        } while(module.getClass() != clazz);

        return module;
    }

}
