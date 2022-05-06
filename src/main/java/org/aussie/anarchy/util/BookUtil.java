package org.aussie.anarchy.util;

import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Messages;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookUtil implements Listener {

    public static void clearBooks(Player player) {
        ItemStack[] var1 = player.getInventory().getContents();

        for (ItemStack item : var1) {
            if (item != null) {
                if (CompatUtil.get().isBook(item)) {
                    stripPages(item);
                }

                if (CompatUtil.get().isShulker(item)) {
                    BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                    ShulkerBox box = (ShulkerBox) meta.getBlockState();
                    ItemStack[] var7 = box.getInventory().getContents();

                    for (ItemStack i : var7) {
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
        List<String> pages = new ArrayList<>();

        for (String page : bookMeta.getPages()) {
            if (page.getBytes(StandardCharsets.UTF_8).length <= 255) {
                pages.add(page);
            }
        }

        bookMeta.setPages(pages);
        book.setItemMeta(bookMeta);
    }

    @EventHandler
    private void on(PlayerEditBookEvent e) {
        for (String page : e.getNewBookMeta().getPages()) {
            for (char c : page.toCharArray()) {
                if (!verifyUnicodeCharacter(c)) {
                    page = page.replace(c, '\u0000');
                }
            }
        }
    }

    public static boolean verifyUnicodeCharacter(char unicode) {
        for (String string : Messages.WHITELISTED_UNICODE) {
            char character = string.toCharArray()[0];

            if (character != unicode) continue;

            return true;
        }
        return false;
    }
}
