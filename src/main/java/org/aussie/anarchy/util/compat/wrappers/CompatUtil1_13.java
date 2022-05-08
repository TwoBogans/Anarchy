package org.aussie.anarchy.util.compat.wrappers;

import org.aussie.anarchy.util.compat.CompatUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CompatUtil1_13 extends CompatUtil {
    public CompatUtil1_13() {
    }

    public boolean isShulker(ItemStack item) {
        switch(item.getType()) {
        case BLACK_SHULKER_BOX:
        case BLUE_SHULKER_BOX:
        case BROWN_SHULKER_BOX:
        case CYAN_SHULKER_BOX:
        case GRAY_SHULKER_BOX:
        case GREEN_SHULKER_BOX:
        case LIGHT_BLUE_SHULKER_BOX:
        case LIME_SHULKER_BOX:
        case MAGENTA_SHULKER_BOX:
        case ORANGE_SHULKER_BOX:
        case PINK_SHULKER_BOX:
        case PURPLE_SHULKER_BOX:
        case LIGHT_GRAY_SHULKER_BOX:
        case RED_SHULKER_BOX:
        case WHITE_SHULKER_BOX:
        case YELLOW_SHULKER_BOX:
            return true;
        default:
            return false;
        }
    }

    public boolean isBook(ItemStack item) {
        switch(item.getType()) {
        case WRITABLE_BOOK:
        case WRITTEN_BOOK:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isHead(ItemStack item) {
        switch(item.getType()) {
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
                return true;
            default:
                return false;
        }
    }

    public boolean isLiquid(Block block) {
        switch(block.getType()) {
        case LAVA:
        case WATER:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isNetherPortal(Block block) {
        return block.getType() == Material.NETHER_PORTAL;
    }
}