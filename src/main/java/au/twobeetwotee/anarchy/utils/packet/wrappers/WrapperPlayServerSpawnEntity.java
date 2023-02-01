package au.twobeetwotee.anarchy.utils.packet.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.reflect.IntEnum;
import au.twobeetwotee.anarchy.utils.packet.AbstractPacket;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class WrapperPlayServerSpawnEntity extends AbstractPacket {
    public static final PacketType TYPE;
    private static PacketConstructor entityConstructor;

    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public WrapperPlayServerSpawnEntity(Entity entity, int type, int objectData) {
        super(fromEntity(entity, type, objectData), TYPE);
    }

    private static PacketContainer fromEntity(Entity entity, int type, int objectData) {
        if (entityConstructor == null) {
            entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, entity, type, objectData);
        }

        return entityConstructor.createPacket(entity, type, objectData);
    }

    public int getEntityID() {
        return this.handle.getIntegers().read(0);
    }

    public Entity getEntity(World world) {
        return this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public UUID getUniqueId() {
        return this.handle.getUUIDs().read(0);
    }

    public void setUniqueId(UUID value) {
        this.handle.getUUIDs().write(0, value);
    }

    public double getX() {
        return this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    public double getOptionalSpeedX() {
        return (double) this.handle.getIntegers().read(1) / 8000.0D;
    }

    public void setOptionalSpeedX(double value) {
        this.handle.getIntegers().write(1, (int)(value * 8000.0D));
    }

    public double getOptionalSpeedY() {
        return (double) this.handle.getIntegers().read(2) / 8000.0D;
    }

    public void setOptionalSpeedY(double value) {
        this.handle.getIntegers().write(2, (int)(value * 8000.0D));
    }

    public double getOptionalSpeedZ() {
        return (double) this.handle.getIntegers().read(3) / 8000.0D;
    }

    public void setOptionalSpeedZ(double value) {
        this.handle.getIntegers().write(3, (int)(value * 8000.0D));
    }

    public float getPitch() {
        return (float) this.handle.getIntegers().read(4) * 360.0F / 256.0F;
    }

    public void setPitch(float value) {
        this.handle.getIntegers().write(4, (int)(value * 256.0F / 360.0F));
    }

    public float getYaw() {
        return (float) this.handle.getIntegers().read(5) * 360.0F / 256.0F;
    }

    public void setYaw(float value) {
        this.handle.getIntegers().write(5, (int)(value * 256.0F / 360.0F));
    }

    public EntityType getType() {
        return this.handle.getEntityTypeModifier().read(0);
    }

    public void setType(EntityType value) {
        this.handle.getEntityTypeModifier().write(0, value);
    }

    public int getObjectData() {
        return this.handle.getIntegers().read(6);
    }

    public void setObjectData(int value) {
        this.handle.getIntegers().write(6, value);
    }

    static {
        TYPE = PacketType.Play.Server.SPAWN_ENTITY;
    }

    public static class ObjectTypes extends IntEnum {
        public static final int BOAT = 1;
        public static final int ITEM_STACK = 2;
        public static final int AREA_EFFECT_CLOUD = 3;
        public static final int MINECART = 10;
        public static final int ACTIVATED_TNT = 50;
        public static final int ENDER_CRYSTAL = 51;
        public static final int TIPPED_ARROW_PROJECTILE = 60;
        public static final int SNOWBALL_PROJECTILE = 61;
        public static final int EGG_PROJECTILE = 62;
        public static final int GHAST_FIREBALL = 63;
        public static final int BLAZE_FIREBALL = 64;
        public static final int THROWN_ENDERPEARL = 65;
        public static final int WITHER_SKULL_PROJECTILE = 66;
        public static final int SHULKER_BULLET = 67;
        public static final int FALLING_BLOCK = 70;
        public static final int ITEM_FRAME = 71;
        public static final int EYE_OF_ENDER = 72;
        public static final int THROWN_POTION = 73;
        public static final int THROWN_EXP_BOTTLE = 75;
        public static final int FIREWORK_ROCKET = 76;
        public static final int LEASH_KNOT = 77;
        public static final int ARMORSTAND = 78;
        public static final int FISHING_FLOAT = 90;
        public static final int SPECTRAL_ARROW = 91;
        public static final int DRAGON_FIREBALL = 93;
        private static final WrapperPlayServerSpawnEntity.ObjectTypes INSTANCE = new WrapperPlayServerSpawnEntity.ObjectTypes();

        public ObjectTypes() {
        }

        public static WrapperPlayServerSpawnEntity.ObjectTypes getInstance() {
            return INSTANCE;
        }
    }
}
