package org.aussie.anarchy.util.packet.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.AutoWrapper;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.aussie.anarchy.util.packet.AbstractPacket;

public class WrapperPlayClientUseItem extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Client.USE_ITEM;

	public WrapperPlayClientUseItem() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientUseItem(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block position
	 * 
	 * @return The current Location
	 */
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * 
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}

	public EnumWrappers.Direction getFace() {
		return handle.getDirections().read(0);
	}

	public void setFace(EnumWrappers.Direction value) {
		handle.getDirections().write(0, value);
	}

	public EnumWrappers.Hand getHand() {
		return handle.getHands().read(0);
	}

	public void setHand(EnumWrappers.Hand value) {
		handle.getHands().write(0, value);
	}

	/**
	 * Retrieve Cursor Position X.
	 * <p>
	 * Notes: the position of the crosshair on the block, from 0 to 15
	 * increasing from west to east
	 * 
	 * @return The current Cursor Position X
	 */
	public float getCursorPositionX() {
		return handle.getFloat().read(0);
	}

	/**
	 * Set Cursor Position X.
	 * 
	 * @param value - new value.
	 */
	public void setCursorPositionX(float value) {
		handle.getFloat().write(0, value);
	}

	/**
	 * Retrieve Cursor Position Y.
	 * <p>
	 * Notes: the position of the crosshair on the block, from 0 to 15
	 * increasing from bottom to top
	 * 
	 * @return The current Cursor Position Y
	 */
	public float getCursorPositionY() {
		return handle.getFloat().read(1);
	}

	/**
	 * Set Cursor Position Y.
	 * 
	 * @param value - new value.
	 */
	public void setCursorPositionY(float value) {
		handle.getFloat().write(1, value);
	}

	/**
	 * Retrieve Cursor Position Z.
	 * <p>
	 * Notes: the position of the crosshair on the block, from 0 to 15
	 * increasing from north to south
	 * 
	 * @return The current Cursor Position Z
	 */
	public float getCursorPositionZ() {
		return handle.getFloat().read(2);
	}

	/**
	 * Set Cursor Position Z.
	 * 
	 * @param value - new value.
	 */
	public void setCursorPositionZ(float value) {
		handle.getFloat().write(2, value);
	}

	public static class MovingObjectPosition {
		public EnumWrappers.Direction direction;
		public BlockPosition position;
		public boolean insideBlock;
	}

	private static final Class<?> POSITION_CLASS = MinecraftReflection.getMinecraftClass("MovingObjectPositionBlock");

	private static final AutoWrapper<MovingObjectPosition> AUTO_WRAPPER = AutoWrapper
			.wrap(MovingObjectPosition.class, POSITION_CLASS)
			.field(0, EnumWrappers.getDirectionConverter())
			.field(1, BlockPosition.getConverter());

	public MovingObjectPosition getPosition() {
		return handle.getModifier().withType(POSITION_CLASS, AUTO_WRAPPER).read(0);
	}

	public void setPosition(MovingObjectPosition position) {
		handle.getModifier().withType(POSITION_CLASS, AUTO_WRAPPER).write(0, position);
	}
}