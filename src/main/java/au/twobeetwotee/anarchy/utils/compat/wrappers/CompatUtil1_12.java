package au.twobeetwotee.anarchy.utils.compat.wrappers;

import au.twobeetwotee.anarchy.utils.compat.CompatUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CompatUtil1_12 extends CompatUtil {
    public CompatUtil1_12() {
    }

    public boolean isShulker(ItemStack item) {
        return switch (item.getType()) {
            case LEGACY_BLACK_SHULKER_BOX, LEGACY_BLUE_SHULKER_BOX, LEGACY_BROWN_SHULKER_BOX,
                 LEGACY_CYAN_SHULKER_BOX, LEGACY_GRAY_SHULKER_BOX, LEGACY_GREEN_SHULKER_BOX,
                 LEGACY_LIGHT_BLUE_SHULKER_BOX, LEGACY_LIME_SHULKER_BOX, LEGACY_MAGENTA_SHULKER_BOX,
                 LEGACY_ORANGE_SHULKER_BOX, LEGACY_PINK_SHULKER_BOX, LEGACY_PURPLE_SHULKER_BOX,
                 LEGACY_SILVER_SHULKER_BOX, LEGACY_RED_SHULKER_BOX, LEGACY_WHITE_SHULKER_BOX,
                 LEGACY_YELLOW_SHULKER_BOX ->
                    true;
            default -> false;
        };
    }

    public boolean isBook(ItemStack item) {
        return switch (item.getType()) {
            case LEGACY_BOOK_AND_QUILL, LEGACY_WRITTEN_BOOK -> true;
            default -> false;
        };
    }

    @Override
    public boolean isHead(ItemStack item) {
        return switch (item.getType()) {
            case LEGACY_SKULL, LEGACY_SKULL_ITEM -> true;
            default -> false;
        };
    }

    public boolean isLiquid(Block block) {
        return switch (block.getType()) {
            case LEGACY_LAVA, LEGACY_STATIONARY_LAVA, LEGACY_WATER, LEGACY_STATIONARY_WATER -> true;
            default -> false;
        };
    }

    @Override
    public boolean isNetherPortal(Block block) {
        return block.getType() == Material.getMaterial("PORTAL");
    }

}
