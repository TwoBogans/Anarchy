package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
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
        if (e.getEntity() instanceof Donkey inventory) {

            if (!inventory.isCarryingChest()) return;

            if (!(e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL)) return;

            // Check Donkey is on Half a Heart
            if (inventory.getHealth() <= 1 && inventory.getEntityId() != this.cloneId) {
                World world = inventory.getWorld();

                world.spawn(inventory.getLocation(), inventory.getClass(), clone -> {
                    this.cloneId = clone.getEntityId();
                    this.copyInventory(inventory, clone);

                    clone.getInventory().setSaddle(inventory.getInventory().getSaddle());
                    clone.setHealth(0.5);
                    clone.setOwner(inventory.getOwner());
                    clone.setAdult();
                });
            }
        }
    }

    @SuppressWarnings({"NullableProblems", "StatementWithEmptyBody"})
    private void copyInventory(ChestedHorse from, ChestedHorse to) {
        if (from instanceof Donkey d1 && to instanceof Donkey d2) {
            d2.setCarryingChest(d1.isCarryingChest());
            d2.getInventory().setContents(d1.getInventory().getContents());
        } else {
            /* Do nothing */
        }
    }
}
