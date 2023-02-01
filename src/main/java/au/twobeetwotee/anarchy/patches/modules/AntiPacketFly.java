package au.twobeetwotee.anarchy.patches.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AntiPacketFly extends Module {
    private final HashMap<UUID, Integer> vl = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return Config.ANTIPACKETFLY;
    }

    @Override
    public Module onEnable() {
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.TELEPORT_ACCEPT) {
            public void onPacketReceiving(PacketEvent event) {
                Player e = event.getPlayer();
                if (!e.isGliding() && !e.isInsideVehicle() && event.getPacketType() == PacketType.Play.Client.TELEPORT_ACCEPT) {
                    Material b = e.getLocation().getBlock().getType();

                    boolean solid = !b.equals(Material.AIR) && (b.isOccluding() || b.isSolid());

                    if (!Config.ANTIPACKETFLYONLYSOLID || solid) {
                        if (AntiPacketFly.this.vl.get(e.getUniqueId()) != null) {
                            if (AntiPacketFly.this.vl.get(e.getUniqueId()) > Config.MAXPACKETFLYVL) {
                                event.setCancelled(true);
                            } else {
                                AntiPacketFly.this.vl.merge(e.getUniqueId(), 1, Integer::sum);
                                AnarchyPatches.getScheduler().runTaskLater(this.plugin, () -> AntiPacketFly.this.vl.put(e.getUniqueId(), AntiPacketFly.this.vl.get(e.getUniqueId()) - 1), 200L);
                            }
                        } else {
                            AntiPacketFly.this.vl.put(e.getUniqueId(), 1);
                            AnarchyPatches.getScheduler().runTaskLater(this.plugin, () -> AntiPacketFly.this.vl.put(e.getUniqueId(), AntiPacketFly.this.vl.get(e.getUniqueId()) - 1), 200L);
                        }
                    }
                }
            }
        });
        return this;
    }
}
