package au.twobeetwotee.anarchy.utils.hook.hooks;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import au.twobeetwotee.anarchy.AnarchyPatches;
import au.twobeetwotee.anarchy.utils.hook.PluginHook;

public class ProtocolLibHook extends PluginHook<AnarchyPatches> {
    public static final String NAME = "ProtocolLib";
    private ProtocolManager protocolManager;

    public ProtocolLibHook(AnarchyPatches plugin) {
        super(plugin, "ProtocolLib");
        ProtocolManager protocol = ProtocolLibrary.getProtocolManager();
        if (protocol == null) {
            plugin.warn("Couldn't detect ProtocolLib!");
        } else {
            this.protocolManager = protocol;
        }
    }

    public void add(PacketListener listener) {
        this.protocolManager.addPacketListener(listener);
    }

    public void remove(AnarchyPatches plugin) {
        this.protocolManager.removePacketListeners(plugin);
    }

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }
}