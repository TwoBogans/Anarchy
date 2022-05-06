package org.aussie.anarchy.util.compat;

import org.aussie.anarchy.util.Util;
import org.aussie.anarchy.util.compat.wrappers.CompatUtil1_12;
import org.aussie.anarchy.util.compat.wrappers.CompatUtil1_13;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public abstract class CompatUtil {
    private static final int SUB_VERSION;

    public CompatUtil() {
    }

    public static boolean is1_12() {
        return SUB_VERSION == 12;
    }

    public static boolean is1_13() {
        return SUB_VERSION > 12;
    }

    public abstract boolean isShulker(ItemStack var1);

    public abstract boolean isBook(ItemStack var1);

    public abstract boolean isLiquid(Block var1);

    public abstract boolean isNetherPortal(Block block);

    public static CompatUtil get() {
        return is1_12() ? new CompatUtil1_12() : new CompatUtil1_13();
    }

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String[] versionInfo = packageName.substring(packageName.lastIndexOf(46) + 1).split("_");
        SUB_VERSION = Util.parseInt(versionInfo[1]).orElse(0);
    }
}
