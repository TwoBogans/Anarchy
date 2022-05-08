package org.aussie.anarchy.module.features;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.World;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.ItemStack;

public class NetherCactusDupe extends Module {

    @Override
    public boolean isEnabled() {
        return Config.CACTUSPORTALDUPE;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(EntityPortalEvent e) {
        if (e.getEntity() instanceof ChestedHorse) {
            ChestedHorse inventory = (ChestedHorse) e.getEntity();

            if (!inventory.isCarryingChest()) return;

            if (!(e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL)) return;

            // Check Donkey is on Half a Heart
            if (inventory.getHealth() <= 1) {
                World world = inventory.getWorld();

                world.spawnEntity(inventory.getLocation(), inventory.getType(), inventory.getEntitySpawnReason(), entity -> {
                    ChestedHorse clone = (ChestedHorse) entity;
                    ItemStack[] items = inventory.getInventory().getContents();

                    clone.setHealth(inventory.getHealth());
                    clone.getInventory().setContents(items);
                    clone.setCustomName(inventory.getCustomName());
                    clone.setCarryingChest(inventory.isCarryingChest());
                    clone.setOwner(inventory.getOwner());
                });
            }
        }
    }

}
