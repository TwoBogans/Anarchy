package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;

public class AntiElytra extends Module {
    private final HashMap<Player, Integer> VL = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTIELYTRAFLY;
    }

    @Override
    public Module onEnable() {
        return this;
    }

//    @EventHandler
//    public void on(EntityToggleGlideEvent evt) {
//        if (isEnabled()) {
//            PlayerInventory i = ((Player)evt.getEntity()).getInventory();
//            if (evt.getEntity() instanceof Player) {
//                Player e = (Player) evt.getEntity();
//
//                if (this.VL.get(e) != null) {
//                    if (this.VL.get(e) > Config.MAXELYTRAFLYVL) {
//                        if (i.getChestplate() != null && i.getChestplate().getType().equals(Material.ELYTRA)) {
//                            ItemStack elytra = i.getChestplate();
//                            i.setChestplate(null);
//                            HashMap<Integer, ItemStack> map = i.addItem(elytra);
//
//                            for (ItemStack is : map.values()) {
//                                evt.getEntity().getWorld().dropItemNaturally(Objects.requireNonNull(i.getLocation()), is);
//                            }
//
//
//                            evt.setCancelled(true);
////                            log(e.getName() + " prevented from using packet elytra fly at " + Util.formattedLocation(e.getLocation()));
//                        }
//                    } else {
//                        this.VL.merge(e, 1, Integer::sum);
//                        Bukkit.getServer().getScheduler().runTaskLater(AnarchyPatches.getPlugin(), () -> this.VL.put(e, this.VL.get(e) - 1), 200L);
//                    }
//                } else {
//                    this.VL.put(e, 1);
//                    Bukkit.getServer().getScheduler().runTaskLater(AnarchyPatches.getPlugin(), () -> this.VL.put(e, this.VL.get(e) - 1), 200L);
//                }
//            }
//        }
//
//    }

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            this.VL.putIfAbsent(player, 1);
            if (e.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
                int i = this.VL.get(player);
                this.VL.put(player, i + 1);
                if (this.VL.get(player) >= 2) {
                    e.setCancelled(true);
                    Bukkit.getServer().getScheduler().runTaskLater(AnarchyPatches.getPlugin(), () -> this.VL.put(player, Math.min(0, this.VL.get(player) - 1)), 200L);
                }
            }
        }
    }
}
