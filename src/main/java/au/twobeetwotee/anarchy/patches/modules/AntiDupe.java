package au.twobeetwotee.anarchy.patches.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import au.twobeetwotee.anarchy.utils.packet.wrappers.WrapperPlayClientEntityAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.HashMap;
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
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent e) {
                WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(e.getPacket());

                Location playerLocation = e.getPlayer().getLocation();

                if (packet.getAction() == EnumWrappers.PlayerAction.OPEN_INVENTORY) {
                    check(packet.getEntity(e), e.getPlayer(), playerLocation);
                }
            }
        });

        AnarchyPatches.getScheduler().scheduleSyncRepeatingTask(AnarchyPatches.getPlugin(), () -> {
            for (UUID uuid : playerLocation.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                Entity entity = Bukkit.getEntity(playerEntity.get(uuid));

                if (entity == null || player == null) {
                    return;
                }

                if (entity.getChunk().getChunkKey() != playerLocation.get(uuid).getChunk().getChunkKey()) {
                    if (player.isInsideVehicle() && player.getVehicle() instanceof ChestedHorse inventory) {

                        Location playerLocation = player.getLocation();

                        if (inventory.getEntityId() != entity.getEntityId()) {
                            check(entity, player, playerLocation);
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

    private void check(Entity entity, Player player, Location loc) {
        if (entity.getChunk().getChunkKey() != playerLocation.get(player.getUniqueId()).getChunk().getChunkKey()) {
            if (player.isInsideVehicle() && player.getVehicle() instanceof ChestedHorse inventory) {

                if (inventory.isCarryingChest() && inventory.isInsideVehicle()) {
                    player.getWorld().loadChunk(inventory.getChunk());
                    inventory.getInventory().clear();
                    inventory.eject();
                    player.teleport(loc);
                }
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
        if (e.getInventory().getHolder() instanceof ChestedHorse inventory) {
            playerLocation.put(e.getPlayer().getUniqueId(), inventory.getLocation());
            playerEntity.put(e.getPlayer().getUniqueId(), inventory.getUniqueId());
        }
    }
}