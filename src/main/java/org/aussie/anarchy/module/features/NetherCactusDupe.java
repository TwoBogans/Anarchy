package org.aussie.anarchy.module.features;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.World;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Donkey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;

public class NetherCactusDupe extends Module {

    @Override
    public boolean isEnabled() {
        return Config.CACTUSPORTALDUPE;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    private int cloneId = -1;

    @EventHandler
    private void on(EntityPortalEvent e) {
        if (e.getEntity() instanceof Donkey) {
            Donkey inventory = (Donkey) e.getEntity();

            if (!inventory.isCarryingChest()) return;

            if (!(e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL)) return;

            // Check Donkey is on Half a Heart
            if (inventory.getHealth() <= 1 && inventory.getEntityId() != this.cloneId) {
                World world = inventory.getWorld();
                world.spawn(inventory.getLocation(), inventory.getClass(), clone -> {
                    this.cloneId = clone.getEntityId();
                    this.copyInventory(inventory, clone);

                    clone.getInventory().setSaddle(inventory.getInventory().getSaddle());
                    clone.setMaxDomestication(inventory.getMaxDomestication());
                    clone.setDomestication(inventory.getDomestication());
                    clone.setJumpStrength(inventory.getJumpStrength());
                    clone.setCustomName(inventory.getCustomName());
                    clone.setHealth(inventory.getHealth());
                    clone.setOwner(inventory.getOwner());
                    clone.setAdult();

                    Anarchy.getScheduler().runTaskLater(Anarchy.getPlugin(), clone::remove, 2L);
                });
            }
        }
    }

    private void copyInventory(ChestedHorse from, ChestedHorse to) {
        if (from instanceof Donkey && to instanceof Donkey) {
            Donkey d1 = (Donkey) from;
            Donkey d2 = (Donkey) to;
            d2.setCarryingChest(d1.isCarryingChest());
            d2.getInventory().setContents(d1.getInventory().getContents());
        } else {
            /* Do nothing */
        }
    }
}
