package org.aussie.anarchy.module.features;

import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;

public class AntiSpam extends Module {
    private final HashMap<Player, String> messages = new HashMap<>();
    private final HashMap<Player, Boolean> moved = new HashMap<>();
    private final HashSet<Player> cooldown = new HashSet<>();

    public boolean isEnabled() {
        return Config.ANTISPAM;
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
    private void on(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        if (!this.bypass(player)) {
            if (message.contains("!tps")) {
                e.setCancelled(true);

                this.log("§cReason: !tps >:( Player: " + player.getName() + " Message: " + message);
            }

            if (this.messages.containsKey(player) && message.equalsIgnoreCase(this.messages.get(player))) {
                e.setCancelled(true);

                this.log("§cReason: duplicate Player: " + player.getName() + " Message: " + message);
            }

            if (!(Boolean) this.moved.getOrDefault(e.getPlayer(), false)) {
                e.setCancelled(true);

                this.log("§cReason: hasn't moved Player: " + player.getName() + " Message: " + message);
            }

            this.messages.put(player, message);

            this.cooldown.add(player);
        }
    }

    private boolean bypass(Player player) {
        return player.hasPermission("anarchy.antispam.bypass") || Util.isAdmin(player);
    }
}
