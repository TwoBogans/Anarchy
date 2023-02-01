package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Halloween extends Module {

    private final Random random = new Random();

    @Override
    public boolean isEnabled() {
        return getTime().getMonthOfYear() == 10 && Config.HALLOWEEN;
    }

    public Module onEnable() {
        return this;
    }

//    private void createExplosion(Block block) {
//        Block below = block.getLocation().add(0.0D, -1.0D, 0.0D).getBlock();
//        if (below.getType() == Material.BEDROCK || below.getType() == Material.OBSIDIAN) {
//            block.setType(Material.AIR);
//            block.getWorld().createExplosion(block.getLocation(), 8.0F, true, true);
//        }
//    }
//
//    @EventHandler
//    public void on(BlockPlaceEvent event) {
//        if (event.getBlock().getType() == Material.PUMPKIN) {
//            this.createExplosion(event.getBlock());
//        }
//    }

    @EventHandler
    public void on(PlayerRespawnEvent event) {
        if (event.getPlayer().getInventory().getHelmet() == null && event.getPlayer().getBedSpawnLocation() == null) {
            if (this.random.nextBoolean()) {
                ItemStack i = new ItemStack(Material.PUMPKIN);
                ItemMeta m = i.getItemMeta();
                m.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                i.setItemMeta(m);
                event.getPlayer().getInventory().setHelmet(i);
            }
        }
    }

//    @EventHandler
//    public void on(PlayerInteractEvent e) {
//        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.PUMPKIN) {
//            this.createExplosion(e.getClickedBlock());
//        }
//    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            if (this.random.nextFloat() >= 0.3F) {
                event.getDrops().add(new ItemStack(Material.PUMPKIN, this.random.nextInt(10)));
            } else if (this.random.nextFloat() <= 0.2F) {
                event.getDrops().add(new ItemStack(Material.PUMPKIN_PIE, this.random.nextInt(10)));
            }
        }
    }

    @EventHandler
    public void on(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity e) {
            EntityEquipment ee = e.getEquipment();
            if (ee != null) ee.setHelmet(new ItemStack(Material.PUMPKIN));
            if (e.getType() == EntityType.HORSE && e.getType() != EntityType.ZOMBIE_HORSE && e.getType() != EntityType.SKELETON_HORSE) {
                event.setCancelled(true);
                EntityType et;
                switch(this.random.nextInt(2)) {
                    case 0:
                        et = EntityType.SKELETON_HORSE;
                        break;
                    case 1:
                        et = EntityType.ZOMBIE_HORSE;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value");
                }

                World w = e.getWorld();
                Location l = e.getLocation();
                w.spawnEntity(l, et);
            }
        }

    }
}
