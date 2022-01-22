package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class AntiProjectileVelocity extends Module {

    @Override
    public boolean isEnabled() {
        return Config.ANTIPROJECTILEVELOCITY;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(EntityShootBowEvent e) {
        if (this.isEnabled()) {
            Entity arrow = e.getProjectile();
            Vector v = arrow.getVelocity();
            arrow.setVelocity(v.setX(Util.clamp(v.getX(), -3.0D, 3.0D)).setY(Util.clamp(v.getY(), -3.0D, 3.0D)).setZ(Util.clamp(v.getZ(), -3.0D, 3.0D)));
        }
    }

    @EventHandler
    private void on(ProjectileLaunchEvent e) {
        if (this.isEnabled()) {
            Entity entity = e.getEntity();
            Vector v = entity.getVelocity();
            if (entity instanceof EnderPearl || entity instanceof Snowball || entity instanceof Egg) {
                entity.setVelocity(v.setX(Util.clamp(v.getX(), -1.5D, 1.5D)).setY(Util.clamp(v.getY(), -1.5D, 1.5D)).setZ(Util.clamp(v.getZ(), -1.5D, 1.5D)));
            }

            if (entity instanceof SplashPotion || entity instanceof LingeringPotion) {
                entity.setVelocity(v.setX(Util.clamp(v.getX(), -0.5D, 0.5D)).setY(Util.clamp(v.getY(), -0.5D, 0.5D)).setZ(Util.clamp(v.getZ(), -0.5D, 0.5D)));
            }

        }
    }
}