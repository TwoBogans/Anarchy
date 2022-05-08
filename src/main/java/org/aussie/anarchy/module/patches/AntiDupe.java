package org.aussie.anarchy.module.patches;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.hook.hooks.ProtocolLibHook;
import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.config.Config;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayClientEntityAction;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayClientVehicleMove;
import org.aussie.anarchy.util.packet.wrappers.WrapperPlayServerSetSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class AntiDupe extends Module {
    private static final HashMap<UUID, Location> playerLocation = new HashMap<>();
    private static final HashMap<UUID, UUID> playerEntity = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTIDUPE;
    }

    @Override
    public Module onEnable() {
        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Client.VEHICLE_MOVE) {
            public void onPacketReceiving(PacketEvent e) {
                WrapperPlayClientVehicleMove packet = new WrapperPlayClientVehicleMove(e.getPacket());

                double x = packet.getX();
                double y = packet.getY();
                double z = packet.getZ();
                float yaw = packet.getYaw();
                float pitch = packet.getPitch();

                Player player = e.getPlayer();
                Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);

                check(player, loc);
            }
        });

        Anarchy.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(Anarchy.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent e) {
                WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(e.getPacket());
                Location playerLocation = e.getPlayer().getLocation();

                double x = playerLocation.getX();
                double y = playerLocation.getY();
                double z = playerLocation.getZ();
                float yaw = playerLocation.getYaw();
                float pitch = playerLocation.getPitch();

                Player player = e.getPlayer();
                Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);

                if (packet.getAction() == EnumWrappers.PlayerAction.OPEN_INVENTORY) {
                    check(player, loc);
                }


            }
        });

        Anarchy.getScheduler().scheduleSyncRepeatingTask(Anarchy.getPlugin(), () -> {
            for (UUID uuid : playerLocation.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                Entity entity = Bukkit.getEntity(playerEntity.get(uuid));

                if (entity == null || player == null) {
                    return;
                }

                if (entity.getChunk().getChunkKey() != playerLocation.get(uuid).getChunk().getChunkKey()) {
                    if (player.isInsideVehicle() && player.getVehicle() instanceof ChestedHorse) {
                        ChestedHorse inventory = (ChestedHorse) player.getVehicle();

                        Location playerLocation = player.getLocation();

                        double x = playerLocation.getX();
                        double y = playerLocation.getY();
                        double z = playerLocation.getZ();
                        float yaw = playerLocation.getYaw();
                        float pitch = playerLocation.getPitch();

                        Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);

                        if (inventory.getEntityId() != entity.getEntityId()) {
                            check(player, loc);
                        }
                    } else {
                        player.closeInventory();
                        entity.eject();
                    }
                }
            }

        }, 1L, 20L);
        return this;
    }

    private void check(Player player, Location loc) {
        if (player.isInsideVehicle() && player.getVehicle() instanceof ChestedHorse) {
            ChestedHorse inventory = (ChestedHorse)player.getVehicle();

            if (inventory.isCarryingChest() && inventory.isInsideVehicle()) {
                inventory.getInventory().clear();
                inventory.eject();
                player.teleport(loc);
            }
        }
    }

    @EventHandler
    private void on(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof ChestedHorse) {
            playerLocation.remove(e.getPlayer().getUniqueId());
            playerEntity.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    private void on(InventoryOpenEvent e) {
        if (e.getInventory().getHolder() instanceof ChestedHorse) {
            ChestedHorse inventory = (ChestedHorse) e.getInventory().getHolder();
            playerLocation.put(e.getPlayer().getUniqueId(), inventory.getLocation());
            playerEntity.put(e.getPlayer().getUniqueId(), inventory.getUniqueId());
        }
    }
}