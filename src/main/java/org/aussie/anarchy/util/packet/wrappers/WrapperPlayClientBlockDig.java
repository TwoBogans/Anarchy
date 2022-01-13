package org.aussie.anarchy.util.packet.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.aussie.anarchy.util.packet.AbstractPacket;

public class WrapperPlayClientBlockDig extends AbstractPacket {
    public static final PacketType TYPE;

    public WrapperPlayClientBlockDig() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientBlockDig(PacketContainer packet) {
        super(packet, TYPE);
    }

    public BlockPosition getLocation() {
        return this.handle.getBlockPositionModifier().read(0);
    }

    public void setLocation(BlockPosition value) {
        this.handle.getBlockPositionModifier().write(0, value);
    }

    public EnumWrappers.Direction getDirection() {
        return this.handle.getDirections().read(0);
    }

    public void setDirection(EnumWrappers.Direction value) {
        this.handle.getDirections().write(0, value);
    }

    public EnumWrappers.PlayerDigType getStatus() {
        return this.handle.getPlayerDigTypes().read(0);
    }

    public void setStatus(EnumWrappers.PlayerDigType value) {
        this.handle.getPlayerDigTypes().write(0, value);
    }

    static {
        TYPE = PacketType.Play.Client.BLOCK_DIG;
    }
}
