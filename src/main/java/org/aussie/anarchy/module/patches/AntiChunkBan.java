package org.aussie.anarchy.module.patches;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AntiChunkBan extends Module {
    private static final Object2LongArrayMap<Block> BLOCKS = new Object2LongArrayMap();

    public AntiChunkBan() {
    }

    public boolean isEnabled() {
        return Config.ANTICHUNKBAN;
    }

    public Module onEnable() {
        get().getScheduler().runTaskTimerAsynchronously(get(), () -> {

            for (Map.Entry<Block, Long> blockLongEntry : BLOCKS.entrySet()) {
                Block block = blockLongEntry.getKey();
                if (block.getType() == Material.AIR) {
                    BLOCKS.remove(block);
                    return;
                }

                if (System.currentTimeMillis() - blockLongEntry.getValue() > TimeUnit.SECONDS.toMillis(Config.ANTICHUNKBANTIME)) {
                    Bukkit.getServer().getScheduler().runTask(get(), () -> block.setType(Material.AIR));
                    this.log("Removed " + block.getType() + " has been removed after " + Config.ANTICHUNKBANTIME + "s");
                    BLOCKS.remove(block);
                }
            }

        }, 1L, 5L);
        return this;
    }

    @EventHandler
    private void on(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Chunk chunk = block.getChunk();
        if (Config.ANTICHUNKBANBLOCKS.contains(block.getType().toString()) && Util.countChunk(chunk, block.getType()) > Config.ANTICHUNKBANMAX) {
            BLOCKS.put(block, System.currentTimeMillis());
        }

    }
}
