package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AntiBurrow extends Module {
    @Override
    public boolean isEnabled() {
        return Config.ANTIBURROW;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location loc = player.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        double yy = loc.getY();

        Material b = loc.getWorld().getBlockAt(x, y, z).getType();

        if (!b.equals(Material.AIR) && b.isOccluding() && !b.equals(Material.SOUL_SAND) && !isGravityBlock(b)) {
            player.damage(Config.ANTIBURROWDAMAGE);
        }

        switch (b) {
            case ENDER_CHEST:
            case SOUL_SAND: {
                if (yy - y < 0.875) {
                    player.damage(Config.ANTIBURROWDAMAGE);
                }
                break;
            }
            case LEGACY_ENCHANTMENT_TABLE:
            case ENCHANTING_TABLE: {
                if (yy - y < 0.75) {
                    player.damage(Config.ANTIBURROWDAMAGE);
                }
                break;
            }
            case BEDROCK:
            case ANVIL:
            case BEACON: {
                player.damage(Config.ANTIBURROWDAMAGE);
                break;
            }

        }
    }

    private Boolean isGravityBlock(Material b) {
        switch (b) {
            case SAND:
            case GRAVEL:
                return true;
            default:
                return false;
        }
    }
}
