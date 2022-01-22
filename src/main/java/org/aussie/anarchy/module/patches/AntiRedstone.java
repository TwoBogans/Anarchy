package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.NotePlayEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AntiRedstone extends Module {
    private final List<Chunk> frozenChunks = new ArrayList<>();
    private final HashMap<Chunk, Integer> currentMap = new HashMap<>();
    private final HashMap<Chunk, Integer> pistonMap = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTIREDSTONE;
    }

    @Override
    public Module onEnable() {
        Anarchy.getPlugin().getScheduler().scheduleSyncRepeatingTask(Anarchy.getPlugin(), this.frozenChunks::clear, 0L, 300L);
        return this;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();

        if (
                Util.getTPS() < Config.ANTIREDSTONETPS ||
                event.getNewCurrent() > Util.getTPS() ||
                this.frozenChunks.contains(chunk) ||
                this.checkChunk(chunk, block.getType())
        ) {
            if (!this.frozenChunks.contains(chunk) && Config.FREEZECHUNK) {
                this.frozenChunks.add(chunk);
            } else {
                event.setNewCurrent(0);
            }

            this.currentMap.putIfAbsent(chunk, event.getNewCurrent());
            this.currentMap.computeIfPresent(chunk, (c, i) -> i + event.getNewCurrent());

            if (this.currentMap.get(chunk) > Config.CHUNKCURRENTMAX) {
                this.frozenChunks.add(chunk);
                this.currentMap.remove(chunk);
            }

//            get().log("Stopped redstone at " + Util.formattedLocation(event.getBlock().getLocation()));
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(BlockPistonExtendEvent e) {
        Chunk chunk = e.getBlock().getChunk();
        Material material = e.getBlock().getType();

        this.pistonMap.putIfAbsent(chunk, 0);
        this.pistonMap.computeIfPresent(chunk, (c, i) -> i++);

        if ((this.checkChunk(chunk, material) || this.pistonMap.get(chunk) > Config.PISTONCHUNKMAX)  && Config.FREEZECHUNK) {
            this.frozenChunks.add(chunk);
        }

        if (Util.getTPS() < Config.ANTIREDSTONETPS || this.frozenChunks.contains(chunk)) {
            e.setCancelled(true);

            if (this.pistonMap.get(chunk) > Config.PISTONCHUNKMAX) {
                this.pistonMap.remove(chunk);
            }

//            get().log("Stopped redstone at " + Util.formattedLocation(e.getBlock().getLocation()));
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(NotePlayEvent e) {
        Chunk chunk = e.getBlock().getChunk();

        if (this.checkChunk(chunk, e.getBlock().getType()) || this.frozenChunks.contains(chunk)) {
            e.setCancelled(true);

            if (!this.frozenChunks.contains(chunk) && Config.FREEZECHUNK) {
                this.frozenChunks.add(chunk);
            }

//            get().log("Stopped redstone at " + Util.formattedLocation(e.getBlock().getLocation()));
        }

    }

    private boolean checkChunk(Chunk chunk, Material material) {
        for (String string : Config.CHUNKFREEZEMATERIALS) {

            String[] split = string.split(":");

            try {
                final Material mat = Material.getMaterial(split[0]);

                if (mat != material) {
                    continue;
                }

                final int maxLimit = Integer.parseInt(split[1]);

                return Util.countChunk(chunk, material) >= maxLimit;
            } catch (Exception e) {
                get().error("Exception: " + e.getMessage());
            }
        }

        return false;
    }
}
