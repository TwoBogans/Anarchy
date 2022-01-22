package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class AntiWitherLag extends Module {

    @Override
    public boolean isEnabled() {
        return Config.ANTIWITHERLAG;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(ChunkLoadEvent e) {
        if (this.isEnabled()) {
            Entity[] var2 = e.getChunk().getEntities();

            for (Entity entity : var2) {
                this.processEntity(entity);
            }
        }

    }

    @EventHandler
    private void on(ChunkUnloadEvent e) {
        if (this.isEnabled()) {
            Entity[] var2 = e.getChunk().getEntities();

            for (Entity entity : var2) {
                this.processEntity(entity);
            }
        }

    }

    @EventHandler
    private void on(CreatureSpawnEvent e) {
        if (this.isEnabled()) {
            Entity entity = e.getEntity();

            if (entity instanceof Wither) {
                Wither wither = (Wither) entity;

                if (Config.ANTIWITHERRANGE <= -1 &&
                    Config.ANTIWITHERWORLDS.contains(wither.getWorld().getName())
                ) {
                    double distance = wither.getLocation().distance(new Location(wither.getWorld(), 0.0D, 128.0D, 0.0D));
                    if (distance < (double) Config.ANTIWITHERRANGE) {
                        get().getScheduler().runTaskLater(get(), wither::remove, 20L);
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    private void on(ProjectileLaunchEvent e) {
        if (this.isEnabled()) {
            this.processEntity(e.getEntity());
        }

    }

    private void processEntity(Entity entity) {
        if (this.isEnabled()) {
            if (entity instanceof WitherSkull) {
                get().getScheduler().runTaskLater(get(), entity::remove, Config.ANTIWITHERSKULLTICKS);
            } else {
                if (entity instanceof Wither) {
                    Wither wither = (Wither) entity;

                    wither.setAI(!(Util.getTPS() <= (double)Config.ANTIWITHERTPS) || Bukkit.getOnlinePlayers().size() <= Config.ANTIWITHERPLAYERS);
                }

            }
        }
    }
}
