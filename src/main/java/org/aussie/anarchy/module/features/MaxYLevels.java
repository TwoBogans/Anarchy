package org.aussie.anarchy.module.features;

import org.aussie.anarchy.module.Module;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MaxYLevels extends Module {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        Location loc = e.getTo();

        if (loc.getBlockY() >= 300) {
            Location newLoc = loc.clone();

            newLoc.setY(256);

            e.setTo(newLoc);
        }
    }
}
