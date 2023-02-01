package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Anniversary extends Module {

    private static final Map<Player, Long> users = new HashMap<>();
    private static boolean enabled = false;

    @Override
    public boolean isEnabled() {
        enabled = getTime().getMonthOfYear() == 1 && (getTime().getDayOfMonth() == 20 || getTime().getDayOfMonth() == 21);
        return enabled || Config.JIHADENABLED;
    }

    @Override
    public Module onEnable() { return this; }

    public static boolean onCommand(CommandSender sender) {
        if (!(enabled || Config.JIHADENABLED)) return false;

        if (sender instanceof Player player) {

            if (users.get(player) == null) {
                addItem(player);
                users.put(player, System.currentTimeMillis());
                return true;
            } else {
                if (users.get(player) + (60 * 1000) <= System.currentTimeMillis()) {
                    addItem(player);
                    return true;
                }
            }

            player.sendMessage(ChatColor.RED + "You have to wait before doing that again m8.");
            return false;
        }

        return false;
    }

    private static void addItem(Player player) {
        ItemStack tnt = new ItemStack(Material.TNT, 64);
        ItemStack lighter = new ItemStack(Material.FLINT_AND_STEEL, 1);

        ItemMeta lighterMeta = lighter.getItemMeta();

        lighterMeta.setDisplayName("ALLAHU AKBAR"); //:-)
        lighterMeta.setLore(Collections.singletonList("Anniversary jihad!"));
        lighterMeta.addEnchant(Enchantment.DURABILITY, 3, false);

        lighter.setItemMeta(lighterMeta);

        player.getInventory().addItem(tnt, lighter);
    }
}
