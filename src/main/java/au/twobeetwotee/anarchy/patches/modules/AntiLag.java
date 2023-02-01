package au.twobeetwotee.anarchy.patches.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.Util;
import au.twobeetwotee.anarchy.utils.config.Config;
import au.twobeetwotee.anarchy.utils.packet.wrappers.WrapperPlayServerSetSlot;
import au.twobeetwotee.anarchy.utils.packet.wrappers.WrapperPlayServerSpawnEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AntiLag extends Module {
    private final HashSet<UUID> vl = new HashSet<>();
    private final HashMap<UUID, Integer> vlChest = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTILAG;
    }

    @Override
    public Module onEnable() {
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (Config.CLEARSHULKERDATA || Util.getTPS() < 10) {
                    WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event.getPacket());

                    // TODO Could add more conditions tps/chest opens
                    if (packet.getWindowId() != 0) {
                        try {
                            ItemStack item = packet.getSlotData().clone();
                            BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                            ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                            shulker.getInventory().clear();
                            shulker.update();

                            meta.setBlockState(shulker);
                            item.setItemMeta(meta);

                            packet.setSlotData(item);
                            event.setPacket(packet.getHandle());
                        } catch (Exception ignored) {
                            // Not shulker
                        }
                    }
                }
            }
        });
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.SPAWN_ENTITY) {
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(event.getPacket());

                Entity e = packet.getEntity(event.getPlayer().getWorld());

                int count = 0;

                for (Entity ce : e.getChunk().getEntities()) {
                    if (ce instanceof Firework) {
                        ++count;
                    }
                }

                if (count > Config.MAXFIREWORKS) {
                    event.setCancelled(true);
                }

            }
        });
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.AUTO_RECIPE) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.AUTO_RECIPE) {
                    if (AntiLag.this.vl.contains(event.getPlayer().getUniqueId())) {
                        event.setCancelled(true);
                    } else {
                        AntiLag.this.vl.add(event.getPlayer().getUniqueId());
                        Bukkit.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                            AntiLag.this.vl.remove(event.getPlayer().getUniqueId());
                        }, 1L);
                    }
                }

            }
        });
        return this;
    }

    @EventHandler
    private void on(VehicleCreateEvent event) {
        if (this.isEnabled()) {
            try {
                Chunk chunk = event.getVehicle().getChunk();
                int amount = 0;
                Entity[] var4 = chunk.getEntities();
                int var5 = var4.length;

                int var6;
                Entity ent;
                for(var6 = 0; var6 < var5; ++var6) {
                    ent = var4[var6];
                    if (ent instanceof Vehicle) {
                        ++amount;
                    }
                }

                if (amount >= Config.MAXMINECARTS) {
                    event.setCancelled(true);
                    var4 = chunk.getEntities();
                    var5 = var4.length;

                    for(var6 = 0; var6 < var5; ++var6) {
                        ent = var4[var6];
                        if (!(ent instanceof Player)) {
                            ent.remove();
                        }
                    }
                }
            } catch (Exception | Error var8) {
                System.out.println(var8);
            }
        }

    }

    @EventHandler
    private void on(VehicleMoveEvent e) {
        if (this.isEnabled()) {
            Chunk chunk = e.getVehicle().getChunk();
            Vehicle vehicle = e.getVehicle();
            if (!e.getFrom().getChunk().equals(e.getTo().getChunk()) && chunk.getEntities().length >= Config.MAXMINECARTS) {
                vehicle.remove();
            }
        }
    }

    @EventHandler
    private void on(ChunkLoadEvent e) {
        var chunk = Arrays.stream(e.getChunk().getEntities());
        var count = new AtomicInteger();
        var maxCount = Util.getTPS() < 19 ? 64 : 128;
        chunk.filter(entity -> entity instanceof Explosive || entity instanceof Projectile).forEach(entity -> {
            count.getAndIncrement();
            if (count.intValue() > maxCount) {
                entity.remove();
            }
        });
    }

    @EventHandler
    private void on(ChunkUnloadEvent e) {
        var chunk = Arrays.stream(e.getChunk().getEntities());
        var count = new AtomicInteger();
        var maxCount = Util.getTPS() < 19 ? 64 : 128;
        chunk.filter(entity -> entity instanceof Explosive || entity instanceof Projectile).forEach(entity -> {
            count.getAndIncrement();
            if (count.intValue() > maxCount) {
                entity.remove();
            }
        });
    }
}
