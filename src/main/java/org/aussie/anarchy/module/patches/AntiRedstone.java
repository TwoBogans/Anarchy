package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Minecart;
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
        Anarchy.getScheduler().scheduleSyncRepeatingTask(Anarchy.getPlugin(), () -> {
            for (Chunk chunk : this.pistonMap.keySet()) {
                if (this.frozenChunks.contains(chunk)) {
                    this.pistonMap.remove(chunk);
                }
            }

            for (Chunk chunk : this.currentMap.keySet()) {
                if (this.frozenChunks.contains(chunk)) {
                    this.currentMap.remove(chunk);
                }
            }

            this.frozenChunks.clear();
        }, 0L, 300L);
        return this;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();

        if (Util.getTPS() < Config.ANTIREDSTONETPS
                || event.getNewCurrent() > Util.getTPS()
                || this.frozenChunks.contains(chunk)
                || this.checkChunk(chunk, block.getType())
        ) {
            if (!this.frozenChunks.contains(chunk) && Config.FREEZECHUNK) {
                this.frozenChunks.add(chunk);
            }

            this.currentMap.putIfAbsent(chunk, event.getNewCurrent());

            this.currentMap.computeIfPresent(chunk, (c, i) -> i + event.getNewCurrent());

            if (this.currentMap.get(chunk) > Config.CHUNKCURRENTMAX) {
                this.frozenChunks.add(chunk);
            }

            event.setNewCurrent(0);

            Anarchy.logToAdmins("[BlockRedstoneEvent] Stopped redstone at " + Util.locationToString(event.getBlock().getLocation()));
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(BlockPistonExtendEvent e) {
        Chunk chunk = e.getBlock().getChunk();

        Material material = e.getBlock().getType();

        Location location = e.getBlock().getLocation();

        checkFallingBlocks(location.add(0, 1, 0), 1);

        this.pistonMap.putIfAbsent(chunk, 1);

        this.pistonMap.computeIfPresent(chunk, (c, i) -> i++);

        if (Util.getTPS() < Config.ANTIREDSTONETPS
                || this.frozenChunks.contains(chunk)
                || this.checkChunk(chunk, material)
                || this.pistonMap.get(chunk) > Config.PISTONCHUNKMAX
        ) {
            if ((!this.frozenChunks.contains(chunk) || this.pistonMap.get(chunk) > Config.PISTONCHUNKMAX) && Config.FREEZECHUNK) {
                this.frozenChunks.add(chunk);
            }

            e.setCancelled(true);

            Anarchy.logToAdmins("[BlockPistonExtendEvent] Stopped redstone at " + Util.locationToString(location));
        }
    }

    private void checkFallingBlocks(Location location, int radius) {
        location.getNearbyEntitiesByType(FallingBlock.class, radius).forEach(Entity::remove);
        location.getNearbyEntitiesByType(Boat.class, radius).forEach(Entity::remove);
        location.getNearbyEntitiesByType(Minecart.class, radius).forEach(Entity::remove);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void on(NotePlayEvent e) {
        Chunk chunk = e.getBlock().getChunk();

        if (this.checkChunk(chunk, e.getBlock().getType()) || this.frozenChunks.contains(chunk)) {
            if (!this.frozenChunks.contains(chunk) && Config.FREEZECHUNK) {
                this.frozenChunks.add(chunk);
            }

            e.setCancelled(true);

            Anarchy.logToAdmins("[NotePlayEvent] Stopped redstone at " + Util.locationToString(e.getBlock().getLocation()));
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
                Anarchy.getPlugin().error("Exception: " + e.getMessage());
            }
        }

        return false;
    }
}
