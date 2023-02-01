package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
        for (Entity entity : e.getChunk().getEntities()) {
            this.processEntity(entity);
        }
    }

    @EventHandler
    private void on(ChunkUnloadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            this.processEntity(entity);
        }
    }

    @EventHandler
    private void on(CreatureSpawnEvent e) {
        Entity entity = e.getEntity();

        if (entity.getType() == EntityType.WITHER) {

            if (Config.ANTIWITHERRANGE == -1) return;

            if (Config.ANTIWITHERWORLDS.contains(entity.getWorld().getName())) {
                double distance = entity.getLocation().distance(new Location(entity.getWorld(), 0.0D, 128.0D, 0.0D));

                if (distance < (double) Config.ANTIWITHERRANGE) {
                    AnarchyPatches.getScheduler().runTaskLater(AnarchyPatches.getPlugin(), entity::remove, 5L);
//                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void on(ProjectileLaunchEvent e) {
        this.processEntity(e.getEntity());
    }

    private void processEntity(Entity entity) {
        if (entity instanceof WitherSkull) {
            AnarchyPatches.getScheduler().runTaskLater(AnarchyPatches.getPlugin(), entity::remove, Config.ANTIWITHERSKULLTICKS);
        } else {
            if (entity instanceof Wither wither) {

                wither.setAI(!(Util.getTPS() <= (double)Config.ANTIWITHERTPS) || Bukkit.getOnlinePlayers().size() <= Config.ANTIWITHERPLAYERS);
            }

        }
    }
}
