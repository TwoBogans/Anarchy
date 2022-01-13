package org.aussie.anarchy.module.features;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashSet;
import java.util.Iterator;

public class RandomSpawn extends Module {
    private final HashSet<Material> disallowedMaterials = new HashSet();

    public RandomSpawn() {
    }

    public boolean isEnabled() {
        return Config.RANDOMSPAWNENABLED;
    }

    public Module onEnable() {

        for (String string : Config.RANDOMSPAWNBLOCKS) {
            Material material = Material.matchMaterial(string);
            if (material == null) {
                this.warn(string + " is an invalid material");
                return this;
            }

            this.disallowedMaterials.add(material);
        }

        return this;
    }

    @EventHandler
    private void on(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (p.getBedSpawnLocation() == null) {
            Location spawn = this.getRandomRespawn();
            if (spawn == null) {
                return;
            }

            e.setRespawnLocation(spawn);
        }

    }

    private Location getRandomRespawn() {
        Location loc = null;

        for(int i = 0; i < 100; ++i) {
            World w = Bukkit.getWorld(Config.RANDOMSPAWNWORLD);
            if (w == null) {
                return null;
            }

            int x = this.randomRange(-Config.RANDOMSPAWNRADIUS, Config.RANDOMSPAWNRADIUS);
            int z = this.randomRange(-Config.RANDOMSPAWNRADIUS, Config.RANDOMSPAWNRADIUS);
            int y = w.getHighestBlockYAt(x, z);
            loc = new Location(w, x, y, z);
            if (!this.disallowedMaterials.contains(loc.add(0.0D, -1.0D, 0.0D).getBlock().getType())) {
                return loc;
            }
        }

        return loc;
    }

    private int randomRange(int min, int max) {
        return min + (int)(Math.random() * (double)(max - min + 1));
    }
}
