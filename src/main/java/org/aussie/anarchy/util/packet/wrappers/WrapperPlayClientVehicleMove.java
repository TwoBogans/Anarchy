package org.aussie.anarchy.util.packet.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.aussie.anarchy.util.packet.AbstractPacket;

public class WrapperPlayClientVehicleMove extends AbstractPacket {
    public static final PacketType TYPE;

    public WrapperPlayClientVehicleMove() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientVehicleMove(PacketContainer packet) {
        super(packet, TYPE);
    }

    public double getX() {
        return this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return (Double)this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return (Double)this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    public float getYaw() {
        return (Float)this.handle.getFloat().read(0);
    }

    public void setYaw(float value) {
        this.handle.getFloat().write(0, value);
    }

    public float getPitch() {
        return (Float)this.handle.getFloat().read(1);
    }

    public void setPitch(float value) {
        this.handle.getFloat().write(1, value);
    }

    static {
        TYPE = PacketType.Play.Client.VEHICLE_MOVE;
    }
}