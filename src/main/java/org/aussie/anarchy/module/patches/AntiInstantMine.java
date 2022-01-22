package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

public class AntiInstantMine extends Module {
    private final HashMap<Player, Long> timer = new HashMap<>();
    private final HashMap<Player, Block> block = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTIINSTANTMINE;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(BlockBreakEvent e) {
        if (this.isEnabled() && this.checkMaterial(e.getBlock().getType())) {
            if (this.block.containsKey(e.getPlayer())) {
                if (this.block.get(e.getPlayer()).getX() == e.getBlock().getX() && this.block.get(e.getPlayer()).getY() == e.getBlock().getY() && this.block.get(e.getPlayer()).getZ() == e.getBlock().getZ()) {
                    if (this.timer.containsKey(e.getPlayer())) {
                        if (System.currentTimeMillis() - this.timer.get(e.getPlayer()) < 5000L) {
                            e.setCancelled(true);
                        } else {
                            this.timer.remove(e.getPlayer());
                            this.timer.put(e.getPlayer(), System.currentTimeMillis());
                        }
                    } else {
                        this.timer.put(e.getPlayer(), System.currentTimeMillis());
                    }
                } else {
                    this.block.remove(e.getPlayer());
                    this.block.put(e.getPlayer(), e.getBlock());
                    this.timer.remove(e.getPlayer());
                    this.timer.put(e.getPlayer(), System.currentTimeMillis());
                }
            } else {
                this.block.put(e.getPlayer(), e.getBlock());
            }
        }

    }

    private boolean checkMaterial(Material material) {
        return Config.INSTANTMINEBLACKLIST.contains(material.name());
    }
}
