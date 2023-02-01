package au.twobeetwotee.anarchy.patches.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.hooks.ProtocolLibHook;
import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import au.twobeetwotee.anarchy.utils.packet.wrappers.WrapperPlayServerWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

public class WitherSound extends Module {

    @Override
    public boolean isEnabled() {
        return Config.WITHERSOUND;
    }

    @Override
    public Module onEnable() {
        AnarchyPatches.getHookManager().getHook(ProtocolLibHook.class).add(new PacketAdapter(AnarchyPatches.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.WORLD_EVENT) {
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerWorldEvent packet = new WrapperPlayServerWorldEvent(event.getPacket());

                if (packet.getEffectId() == 1023) {
                    packet.setDisableRelativeVolume(false);
                }
            }
        });
        return this;
    }

    @EventHandler
    private void on(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.WITHER) {
            for (Player p : AnarchyPatches.getPlugin().getServer().getOnlinePlayers()) {
                if (!e.getLocation().getWorld().equals(p.getWorld())) continue;
                if (e.getEntity().getLocation().distance(p.getLocation()) <= (double) (Bukkit.getViewDistance() * 16 - 2)) {
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
                }
            }
        }
    }
}