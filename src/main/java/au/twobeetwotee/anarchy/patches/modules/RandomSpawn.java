package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashSet;

public class RandomSpawn extends Module {
    private final HashSet<Material> disallowedMaterials = new HashSet<>();

    @Override
    public boolean isEnabled() {
        return Config.RANDOMSPAWNENABLED;
    }

    @Override
    public Module onEnable() {
        for (String string : Config.RANDOMSPAWNBLOCKS) {
            Material material = Material.matchMaterial(string);

            if (material == null) {
                this.warn(string + " is an invalid material");
                continue;
            }

            this.disallowedMaterials.add(material);
        }
        return this;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void on(PlayerRespawnEvent e) {
        e.setRespawnLocation(getRespawnLocation(e.getPlayer()));
    }

    private Location getRespawnLocation(Player player) {
        return player.getBedSpawnLocation() == null ? getRandomRespawn() : player.getBedSpawnLocation();
    }

    private Location getRandomRespawn() {
        synchronized (this) {
            while (true) {
                World w = Bukkit.getWorld(Config.RANDOMSPAWNWORLD);

                if (w != null) {
                    int x = this.randomRange();
                    int z = this.randomRange();

                    Location spawn = new Location(w, x, w.getHighestBlockYAt(x, z), z);

                    if (w.getNearbyPlayers(spawn, 64).size() >= 1) {
                        continue;
                    }

                    Location below = spawn.add(0.0D, -1.0D, 0.0D);

                    if (disallowedMaterials.contains(below.getBlock().getType())) {
                        continue;
                    }

                    return spawn;
                }
            }
        }
    }

    private int randomRange() {
        return random.nextInt(Config.RANDOMSPAWNRADIUSMAX - Config.RANDOMSPAWNRADIUSMIN) + Config.RANDOMSPAWNRADIUSMIN;
    }
}
