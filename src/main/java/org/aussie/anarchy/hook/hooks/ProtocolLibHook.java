package org.aussie.anarchy.hook.hooks;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import org.aussie.anarchy.Anarchy;
import org.aussie.anarchy.util.hook.PluginHook;

public class ProtocolLibHook extends PluginHook<Anarchy> {
    public static final String NAME = "ProtocolLib";
    private ProtocolManager protocolManager;

    public ProtocolLibHook(Anarchy plugin) {
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

    public void remove(Anarchy plugin) {
        this.protocolManager.removePacketListeners(plugin);
    }

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }
}