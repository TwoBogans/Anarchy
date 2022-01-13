package org.aussie.anarchy.util;

import org.aussie.anarchy.util.compat.CompatUtil;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookUtil {
    public BookUtil() {
    }

    public static void clearBooks(Player player) {
        ItemStack[] var1 = player.getInventory().getContents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack item = var1[var3];
            if (item != null) {
                if (CompatUtil.get().isBook(item)) {
                    stripPages(item);
                }

                if (CompatUtil.get().isShulker(item)) {
                    BlockStateMeta meta = (BlockStateMeta)item.getItemMeta();
                    ShulkerBox box = (ShulkerBox)meta.getBlockState();
                    ItemStack[] var7 = box.getInventory().getContents();
                    int var8 = var7.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        ItemStack i = var7[var9];
                        if (i != null && CompatUtil.get().isBook(i)) {
                            stripPages(i);
                        }
                    }

                    box.update();
                    meta.setBlockState(box);
                    item.setItemMeta(meta);
                }
            }
        }

    }

    private static void stripPages(ItemStack book) {
        BookMeta bookMeta = (BookMeta)book.getItemMeta();
        List<String> pages = new ArrayList();
        Iterator var3 = bookMeta.getPages().iterator();

        while(var3.hasNext()) {
            String page = (String)var3.next();
            if (page.getBytes(StandardCharsets.UTF_8).length <= 255) {
                pages.add(page);
            }
        }

        bookMeta.setPages(pages);
        book.setItemMeta(bookMeta);
    }
}
