package org.aussie.anarchy.module;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.module.features.*;
import org.aussie.anarchy.module.patches.*;
import org.aussie.anarchy.module.seasonal.Anniversary;
import org.aussie.anarchy.module.seasonal.Halloween;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager(Anarchy plugin) {
        this.addModules(new AntiSpam(), new MaxYLevels(), new Miscellaneous(), new NoGlobalThunder(), new RandomMOTD(), new SpeedLimit(), new TabManager(), new VeteranCheck(), new WitherSound(), new WorldStats(), new AntiBedrockHoles(), new AntiBoatFly(), new AntiBoatFly(), new AntiChunkBan(), new AntiDupe(), new AntiElytra(), new AntiExploits(), new AntiGodMode(), new AntiInstantMine(), new AntiLag(), new AntiLiquidLag(), new AntiNoCom(), new AntiPacketFly(), new AntiProjectileVelocity(), new AntiRedstone(), new AntiWitherLag(), new Anniversary(), new Halloween());
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

    public Module getModuleByClass(Class<?> clazz) {
        Iterator<Module> var2 = this.modules.iterator();

        Module module;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            module = var2.next();
        } while(module.getClass() != clazz);

        return module;
    }

    public List<Module> getModules() {
        return this.modules;
    }
}
