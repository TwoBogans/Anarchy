package org.aussie.anarchy.module.patches;

import com.au2b2t.anarchyplugin.AnarchyPlugin;
import com.au2b2t.anarchyplugin.util.Util;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

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

    @EventHandler
    public void on(EntityToggleGlideEvent evt) {
        if (isEnabled()) {
            PlayerInventory i = ((Player)evt.getEntity()).getInventory();
            if (evt.getEntity() instanceof Player) {
                Player e = (Player)evt.getEntity();
                if (this.VL.get(e) != null) {
                    if (this.VL.get(e) > Config.MAXELYTRAFLYVL) {
                        if (i.getChestplate() != null && i.getChestplate().getType().equals(Material.ELYTRA)) {
                            ItemStack elytra = i.getChestplate();
                            i.setChestplate(null);
                            HashMap<Integer, ItemStack> map = i.addItem(elytra);

                            for (ItemStack is : map.values()) {
                                evt.getEntity().getWorld().dropItemNaturally(Objects.requireNonNull(i.getLocation()), is);
                            }

                            evt.setCancelled(true);
//                            get().log(e.getName() + " prevented from using packet elytra fly at " + Util.formattedLocation(e.getLocation()));
                        }
                    } else {
                        this.VL.merge(e, 1, Integer::sum);
                        Bukkit.getServer().getScheduler().runTaskLater(AnarchyPlugin.PLUGIN, () -> {
                            this.VL.put(e, this.VL.get(e) - 1);
                        }, 200L);
                    }
                } else {
                    this.VL.put(e, 1);
                    Bukkit.getServer().getScheduler().runTaskLater(AnarchyPlugin.PLUGIN, () -> {
                        this.VL.put(e, this.VL.get(e) - 1);
                    }, 200L);
                }
            }
        }

    }
}
