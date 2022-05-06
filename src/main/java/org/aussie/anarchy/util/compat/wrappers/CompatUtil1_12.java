package org.aussie.anarchy.util.compat.wrappers;

import org.aussie.anarchy.util.compat.CompatUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CompatUtil1_12 extends CompatUtil {
    public CompatUtil1_12() {
    }

    public boolean isShulker(ItemStack item) {
        switch(item.getType()) {
        case LEGACY_BLACK_SHULKER_BOX:
        case LEGACY_BLUE_SHULKER_BOX:
        case LEGACY_BROWN_SHULKER_BOX:
        case LEGACY_CYAN_SHULKER_BOX:
        case LEGACY_GRAY_SHULKER_BOX:
        case LEGACY_GREEN_SHULKER_BOX:
        case LEGACY_LIGHT_BLUE_SHULKER_BOX:
        case LEGACY_LIME_SHULKER_BOX:
        case LEGACY_MAGENTA_SHULKER_BOX:
        case LEGACY_ORANGE_SHULKER_BOX:
        case LEGACY_PINK_SHULKER_BOX:
        case LEGACY_PURPLE_SHULKER_BOX:
        case LEGACY_SILVER_SHULKER_BOX:
        case LEGACY_RED_SHULKER_BOX:
        case LEGACY_WHITE_SHULKER_BOX:
        case LEGACY_YELLOW_SHULKER_BOX:
            return true;
        default:
            return false;
        }
    }

    public boolean isBook(ItemStack item) {
        switch(item.getType()) {
        case LEGACY_BOOK_AND_QUILL:
        case LEGACY_WRITTEN_BOOK:
            return true;
        default:
            return false;
        }
    }

    public boolean isLiquid(Block block) {
        switch(block.getType()) {
        case LEGACY_LAVA:
        case LEGACY_STATIONARY_LAVA:
        case LEGACY_WATER:
        case LEGACY_STATIONARY_WATER:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isNetherPortal(Block block) {
        return block.getType() == Material.getMaterial("PORTAL");
    }

}
