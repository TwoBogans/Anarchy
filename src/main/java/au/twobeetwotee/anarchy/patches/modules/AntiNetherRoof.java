package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.compat.CompatUtil;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class AntiNetherRoof extends Module {

    @Override
    public boolean isEnabled() {
        DateTime now = DateTime.now(DateTimeZone.forID("Australia/Sydney"));
        return Config.ANTINETHERROOF || now.getYear() >= 2023;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location l = p.getLocation();
        if (!p.isOp()) {
            if (l.getWorld().getName().equals("world_nether") && l.getY() >= 125.0D) {
                if (p.isInsideVehicle()) {
                    p.leaveVehicle();
                }

                if (p.isFlying() || p.isGliding()) {
                    e.setCancelled(true);
                }

                Location newLoc = new Location(l.getWorld(), l.getBlockX() + 0.5, 120, l.getBlockZ() + 0.5);

                if (newLoc.getBlock().getType() != Material.AIR) {
                    newLoc.getBlock().setType(Material.AIR);
                }

                Location newLocUp = newLoc.add(0, 1, 0);

                if (newLocUp.getBlock().getType() != Material.AIR) {
                    newLocUp.getBlock().setType(Material.AIR);
                }

                e.setTo(newLoc);
            }
        }
    }

    @EventHandler
    public void on(EntityToggleGlideEvent e) {
        Player p = (Player) e.getEntity();
        Location l = p.getLocation();
        if (!p.isOp()) {
            if (l.getWorld().getName().equals("world_nether") && l.getY() >= 125.0D) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(VehicleMoveEvent e) {
        Location l = e.getVehicle().getLocation();
        if (l.getWorld().getName().equals("world_nether") && l.getY() >= 125.0D) {
            e.getVehicle().eject();
        }
    }

    private void deactivatePortalsAboveRoof(Chunk chunk) {
        if (chunk.getWorld().getEnvironment() != World.Environment.NETHER) return;
        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                for(int y = 0; y < 128; ++y) {
                    Block block = chunk.getBlock(x, y, z);

                    if (CompatUtil.get().isNetherPortal(block)) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
