package org.aussie.anarchy.module.features;

import net.pistonmaster.pistonchat.api.PistonChatEvent;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;

public class AntiSpam extends Module {
    private final HashMap<Player, String> messages = new HashMap();
    private final HashMap<Player, Boolean> moved = new HashMap();
    private final HashSet<Player> cooldown = new HashSet();

    public AntiSpam() {
    }

    public boolean isEnabled() {
        return false;
    }

    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        if (!this.bypass(e.getPlayer())) {
            if (e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
                this.moved.put(e.getPlayer(), true);
            }

        }
    }

    @EventHandler
    private void on(PlayerLoginEvent e) {
        if (!this.bypass(e.getPlayer())) {
            this.moved.putIfAbsent(e.getPlayer(), false);
        }
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        if (!this.bypass(e.getPlayer())) {
            this.moved.put(e.getPlayer(), false);
        }
    }

    @EventHandler
    private void on(PistonChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (!this.bypass(player)) {
            if (this.cooldown.contains(player)) {
                e.setCancelled(true);
                get().getScheduler().scheduleSyncRepeatingTask(get(), () -> {
                    this.cooldown.remove(player);
                }, 0L, 20L);
                this.log("&cReason: cooldown. Player: " + player.getName() + " Message: " + message);
            }

            if (this.messages.containsKey(player) && message.equalsIgnoreCase((String)this.messages.get(player))) {
                e.setCancelled(true);
                this.log("&cReason: duplicate. Player: " + player.getName() + " Message: " + message);
            }

            if (!(Boolean)this.moved.getOrDefault(e.getPlayer(), false)) {
                e.setCancelled(true);
                this.log("&cReason: hasn't moved. Player: " + player.getName() + " Message: " + message);
            }

            this.messages.put(player, message);
            this.cooldown.add(player);
        }
    }

    private boolean bypass(Player player) {
        if (Util.containsDomain(player, "debug.2b2t.com.au")) {
            return false;
        } else {
            return player.hasPermission("anarchy.antispam.bypass") || Util.isAdmin(player);
        }
    }
}
