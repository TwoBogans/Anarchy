package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.SparkHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.ArrayList;
import java.util.List;

public class AntiLiquidLag extends Module {
    private final List<Chunk> frozenChunks = new ArrayList<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTILIQUIDLAG;
    }

    @Override
    public Module onEnable() {
        Anarchy.getScheduler().scheduleSyncRepeatingTask(Anarchy.getPlugin(), this.frozenChunks::clear, 0L, 300L);
        return this;
    }

    @EventHandler
    private void on(BlockFromToEvent e) {
        Block block = e.getBlock();

        SparkHook spark = Anarchy.getHookManager().getHook(SparkHook.class);

        if (CompatUtil.get().isLiquid(block) && (!block.getChunk().isLoaded()
                || block.getLocation().getNearbyPlayers(Config.ANTILIQUIDLAGRADIUS).isEmpty()
                || spark.getTPS_10SEC() <= (double)Config.ANTILIQUIDLAGTPS
                || this.frozenChunks.contains(block.getChunk())
        )) {
           if (!this.frozenChunks.contains(block.getChunk())
                    && (spark.getTPS_10SEC() <= (double)Config.ANTILIQUIDLAGTPS
                    || block.getWorld().getEnvironment() == World.Environment.NETHER)
            ) {
                this.frozenChunks.add(block.getChunk());
            }

            e.setCancelled(true);
        }
    }
}