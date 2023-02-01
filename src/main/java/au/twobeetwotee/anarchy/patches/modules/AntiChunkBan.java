package au.twobeetwotee.anarchy.patches.modules;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AntiChunkBan extends Module {
    private static final Object2LongArrayMap<Block> BLOCKS = new Object2LongArrayMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTICHUNKBAN;
    }

    @Override
    public Module onEnable() {
        AnarchyPatches.getScheduler().runTaskTimer(AnarchyPatches.getPlugin(), () -> {
            for (Map.Entry<Block, Long> blockLongEntry : BLOCKS.entrySet()) {
                Block block = blockLongEntry.getKey();
                if (block.getType() == Material.AIR) {
                    BLOCKS.remove(block);
                    return;
                }

                if (System.currentTimeMillis() - blockLongEntry.getValue() > TimeUnit.SECONDS.toMillis(Config.ANTICHUNKBANTIME)) {
                    Bukkit.getServer().getScheduler().runTask(AnarchyPatches.getPlugin(), () -> block.setType(Material.AIR));
                    this.log("Removed " + block.getType() + " has been removed after " + Config.ANTICHUNKBANTIME + "s");
                    BLOCKS.remove(block);
                }
            }

        }, 0L, 5L);
        return this;
    }

    @EventHandler
    private void on(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Chunk chunk = block.getChunk();

        if (Config.ANTICHUNKBANBLOCKS.contains(block.getType().toString()) && Util.countChunk(chunk, block.getType()) > Config.ANTICHUNKBANMAX) {
            AnarchyPatches.getScheduler().runTask(AnarchyPatches.getPlugin(), () -> {
                for (int x = 0; x < 16; ++x) {
                    for (int z = 0; z < 16; ++z) {
                        for (int y = 0; y < 256; ++y) {
                            Block b = chunk.getBlock(x, y, z);
                            if (b.getType().equals(block.getType())) {
                                b.setType(Material.AIR);
                            }
                        }
                    }
                }
            });
        }

    }
}
