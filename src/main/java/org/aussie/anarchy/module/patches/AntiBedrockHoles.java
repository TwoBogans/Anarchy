package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.SignUtil;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AntiBedrockHoles extends Module {

    @Override
    public boolean isEnabled() {
        return Config.ANTIBEDROCKHOLES && CompatUtil.is1_12();
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(ChunkLoadEvent evt) {
        if (this.isEnabled()) {
            Chunk c = evt.getChunk();

            if (!evt.isNewChunk() && !c.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                int cx = c.getX() << 4;
                int cz = c.getZ() << 4;

                for(int x = cx; x < cx + 16; ++x) {
                    for(int z = cz; z < cz + 16; ++z) {
                        for(int y = 0; y < 1; ++y) {
                            if (c.getBlock(x, y, z).getType() != Material.BEDROCK) {
                                c.getBlock(x, y, z).setType(Material.BEDROCK);
                            }
                        }
                    }
                }
            }
        }

    }
}