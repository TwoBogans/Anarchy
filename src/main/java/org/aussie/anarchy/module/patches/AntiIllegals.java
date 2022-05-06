package org.aussie.anarchy.module.patches;

import org.aussie.anarchy.module.Module;
import org.aussie.anarchy.util.compat.CompatUtil;
import org.aussie.anarchy.util.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class AntiIllegals extends Module {

    private static final int maxLoreEnchantmentLevel = 1;

    @Override
    public boolean isEnabled() {
        return Config.ANTIILLEGALS;
    }

    @Override
    public Module onEnable() {
        return this;
    }

    public void checkInventory(Inventory inventory, Location location, boolean checkRecursive) {
        checkInventory(inventory, location, checkRecursive, false);
    }

    /**
     * use this method to check and remove illegal items from inventories
     *
     * @param inventory      the inventory that should be checked
     * @param location       location of the inventory holder for possible item drops
     * @param checkRecursive true, if items inside containers should be checked
     */
    public void checkInventory(Inventory inventory, Location location, boolean checkRecursive, boolean isInsideShulker) {
        List<ItemStack> removeItemStacks = new ArrayList<>();

        boolean wasFixed = false;
        int fixesIllegals = 0;

        // Loop through Inventory
        for (ItemStack itemStack : inventory.getContents()) {
            switch (checkItemStack(itemStack, location, checkRecursive)) {
                case illegal:
                    removeItemStacks.add(itemStack);
                    break;

                case wasFixed:
                    wasFixed = true;
                    break;

                default:
                    break;
            }
        }

        // Remove illegal items - TODO: check if that is needed if setAmount(0) is in place
        for (ItemStack itemStack : removeItemStacks) {
            itemStack.setAmount(0);
            inventory.remove(itemStack);
            fixesIllegals++;
        }

    }

    /**
     * Check an item and try to fix it. If it is an illegal item, then remove it.
     *
     * @param itemStack      Item
     * @param location       Location for item drops
     * @param checkRecursive True, if inventories of containers should be checked
     * @return State of the Item
     */
    public ItemState checkItemStack(ItemStack itemStack, Location location, boolean checkRecursive) {
        boolean wasFixed = false;

        // null Item
        if (itemStack == null) return ItemState.empty;

        //Name Color Check
        if(itemStack.getType() != Material.WRITTEN_BOOK) {
            if (itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.stripColor(itemMeta.getDisplayName()));
                itemStack.setItemMeta(itemMeta);
                wasFixed = true;
            }
        }

//        if (itemStack.getType() == Material.POTION) {
//            Potion potion = Potion.fromItemStack(itemStack);
//
//            System.out.println("Potion isSplash: " + potion.isSplash());
//
//            if (potion.getLevel() > 4) {
//                itemStack.setAmount(0);
//                wasFixed = true;
//                System.out.println("Potion over lvl 4");
//            }
//        }

//        if (!itemStack.getEnchantments().isEmpty())

        // Lore
        if (itemStack.getItemMeta() != null) {
            if (itemStack.getItemMeta().hasLore()) {
                if (itemStack.getItemMeta().getLore().contains("discord.gg/australiamc")) {
                    ItemMeta meta = itemStack.getItemMeta();

                    String[] lore = new String[]{"discord.gg/fvSKpbtQAV"};

                    meta.setLore(Arrays.asList(lore.clone()));

                    itemStack.setItemMeta(meta);

                    wasFixed = true;
                }
            }
        }

        // Unbreakables
        if (itemStack.getType().isItem() && !itemStack.getType().isEdible() && !itemStack.getType().isBlock()) {

            short maxDurability = itemStack.getType().getMaxDurability(); ItemMeta meta = itemStack.getItemMeta();

            if (itemStack.getDurability() > maxDurability || itemStack.getDurability() < 0 || meta.isUnbreakable()) {
                if (Checks.isWeapon(itemStack) || Checks.isArmor(itemStack)) {
                    meta.setUnbreakable(false);

                    itemStack.setDurability((short) 0);

                    wasFixed = itemStack.setItemMeta(meta);
                }
            }
        }

        // Illegal Blocks
        if (Checks.isIllegalBlock(itemStack.getType())) {
            itemStack.setAmount(0);
            wasFixed = true;
        }

        // nbt furnace check
        if (itemStack.getType() == Material.FURNACE && itemStack.toString().contains("internal=")) {
            itemStack.setAmount(0);
            wasFixed = true;
        }

        // Revert Overstacked Items
        if (itemStack.getAmount() > itemStack.getMaxStackSize()) {
            itemStack.setAmount(itemStack.getMaxStackSize());
            wasFixed = true;
        }

        // Conflicting enchantments
        // We need to check if the enchantment we are checking for conflicts is the same as the one we are checking as it will conflict with itself
        if (Checks.isArmor(itemStack) || Checks.isWeapon(itemStack)) {
            // shuffle key set for random over enchantment removal
            List<Enchantment> keys = new ArrayList<>(itemStack.getEnchantments().keySet());
            Collections.shuffle(keys);

            // no for each loop to prevent concurrent modification exceptions
            for (int kI1 = 0; kI1 < keys.size(); kI1++) {
                for (int kI2 = kI1 + 1; kI2 < keys.size(); kI2++) {
                    Enchantment e1 = keys.get(kI1);

                    if (e1.conflictsWith(keys.get(kI2))) {
                        itemStack.removeEnchantment(e1);
                        //log("checkItem", "Removing conflicting enchantment " + e1.getName() + " from " + itemStack.getType());
                        keys.remove(e1);
                        if (kI1 > 0) {
                            // check next item
                            kI1--;
                            break;
                        }
                    }
                }
            }
        }

        // Max Enchantment
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (!enchantment.canEnchantItem(itemStack) && !Checks.isArmor(itemStack) && !Checks.isWeapon(itemStack) && itemStack.getEnchantmentLevel(enchantment) > maxLoreEnchantmentLevel) {
                // enforce lore enchantments level
                wasFixed = true;

                itemStack.removeEnchantment(enchantment);
                itemStack.addUnsafeEnchantment(enchantment, maxLoreEnchantmentLevel);
            } else if (itemStack.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel()) {
                // enforce max enchantment level
                wasFixed = true;

                itemStack.removeEnchantment(enchantment);
                itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
            }
        }

        // ShulkerBox Check
        if(itemStack.getType().toString().contains("SHULKER_BOX")) {
            if (checkRecursive && itemStack.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta blockMeta = (BlockStateMeta) itemStack.getItemMeta();

                if (blockMeta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox shulker = (ShulkerBox) blockMeta.getBlockState();

                    Inventory inventoryShulker = shulker.getInventory();

                    checkInventory(inventoryShulker, location, true, true);

                    ItemStack[] contents = inventoryShulker.getContents();

                    shulker.getInventory().setContents(contents);
                    blockMeta.setBlockState(shulker);

                    // JsonParseException
                    try {
                        itemStack.setItemMeta(blockMeta);
                        wasFixed = true;
                    } catch (Exception e) {
                        log("checkItem", "Exception " + e.getMessage());
                    }
                }
            }
        }

        return wasFixed ? ItemState.wasFixed : ItemState.clean;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof InventoryHolder)) return;

        // inventory of the block
        Inventory inventory = ((InventoryHolder) event.getBlock().getState()).getInventory();
        Location location = event.getBlock().getLocation();

        checkInventory(inventory, location, true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlaceBlock(BlockPlaceEvent event) {
        checkItemStack(event.getItemInHand(), event.getPlayer().getLocation(), true);

        // placed block - stop placing if its an illegal
        if (Checks.isIllegalBlock(event.getBlockPlaced().getType())) {
            if (event.getItemInHand().getType() != ((CompatUtil.is1_12()) ? Material.getMaterial("EYE_OF_ENDER") : Material.ENDER_EYE)) {
                event.setCancelled(true);
            }
            log(event.getEventName(), "Stopped " + event.getPlayer().getName() + " from placing " + event.getBlockPlaced() + "");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.getVehicle() instanceof InventoryHolder) {
            // inventory of the vehicle
            Inventory inventory = ((InventoryHolder) event.getVehicle()).getInventory();
            Location location = event.getVehicle().getLocation();

            checkInventory(inventory, location, true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        checkItemStack(event.getItemDrop().getItemStack(), event.getItemDrop().getLocation(), true);
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (checkItemStack(event.getItem().getItemStack(), player.getLocation(), true) == AntiIllegals.ItemState.illegal) {
            event.setCancelled(true);
            log(event.getEventName(), "Stopped " + event.getEntity().getName() + " from picking up an illegal item");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getDrops().isEmpty()) return;

        for (ItemStack drop : event.getDrops()) {
            checkItemStack(drop, event.getEntity().getLocation(), false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (event.getMainHandItem() == null)
            if (checkItemStack(event.getMainHandItem(), event.getPlayer().getLocation(), true) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);

        if (event.getOffHandItem() == null)
            if (checkItemStack(event.getOffHandItem(), event.getPlayer().getLocation(), true) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null)
            if (checkItemStack(event.getPlayer().getInventory().getItem(event.getNewSlot()), event.getPlayer().getLocation(), true) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);

        if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()) != null)
            if (checkItemStack(event.getPlayer().getInventory().getItem(event.getPreviousSlot()), event.getPlayer().getLocation(), true) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (checkItemStack(event.getItem(), event.getSource().getLocation(), true) == AntiIllegals.ItemState.illegal)
            event.setCancelled(true);
    }

    @SuppressWarnings("IsCancelled")
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Item Frame check only
        if (event.getRightClicked() instanceof ItemFrame) {

            ItemStack mainHandStack = event.getPlayer().getInventory().getItemInMainHand();

            if (checkItemStack(mainHandStack, event.getPlayer().getLocation(), false) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);

            ItemStack offhandHandStack = event.getPlayer().getInventory().getItemInOffHand();

            if (checkItemStack(offhandHandStack, event.getPlayer().getLocation(), false) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);

            ItemStack frameStack = ((ItemFrame) event.getRightClicked()).getItem();

            if (checkItemStack(frameStack, event.getPlayer().getLocation(), false) == AntiIllegals.ItemState.illegal)
                event.setCancelled(true);

            if (event.isCancelled())
                log(event.getEventName(), "Stopped " + event.getPlayer().getName() + " from placing an illegal item in an item frame");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingBreak(HangingBreakEvent event) {
        if (!(event.getEntity() instanceof ItemFrame)) return;

        ItemStack item = ((ItemFrame) event.getEntity()).getItem();

        if (checkItemStack(item, event.getEntity().getLocation(), true) == AntiIllegals.ItemState.illegal) {
            event.setCancelled(true);
            ((ItemFrame) event.getEntity()).setItem(new ItemStack(Material.AIR));
            log(event.getEventName(), "Deleted Illegal from " + event.getEntity().getName());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // only if an item frame get hit
        if (!(event.getEntity() instanceof ItemFrame)) return;

        ItemFrame itemFrame = (ItemFrame) event.getEntity();

        if (checkItemStack(itemFrame.getItem(), event.getEntity().getLocation(), false) == AntiIllegals.ItemState.illegal) {
            itemFrame.setItem(new ItemStack(Material.AIR));
            log(event.getEventName(), "Removed illegal item from " + itemFrame.toString());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        if (!(event.getWhoClicked() instanceof Player)) return;

        if (checkItemStack(event.getCurrentItem(), event.getWhoClicked().getLocation(), true) == AntiIllegals.ItemState.illegal)
            event.setCancelled(true);

        if (checkItemStack(event.getCursor(), event.getWhoClicked().getLocation(), true) == AntiIllegals.ItemState.illegal)
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().equals(event.getPlayer().getEnderChest())) return;

        checkInventory(event.getInventory(), event.getPlayer().getLocation(), true);
    }

    // from cloudanarchy core
    // dropper / dispenser
    // This event does not get canceled on purpose because the item handling on event cancel is so wonky!
    @EventHandler(ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (checkItemStack(event.getItem(), event.getBlock().getLocation(), false) == AntiIllegals.ItemState.illegal) {
            event.setCancelled(true);
            event.setItem(new ItemStack(Material.AIR));
            event.getBlock().getState().update(true, false);
            log(event.getEventName(), "Stopped dispensing of an illegal block.");
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
            if (effect.getAmplifier() > 5 || effect.getDuration() >= 32760) {
                e.getPlayer().removePotionEffect(effect.getType());
            }
        }
    }

    public void log(String module, String message) {
        log("§a[" + module + "] §e" + message + "§r");
    }

    public enum ItemState {
        empty, clean, wasFixed, illegal
    }

    public static class Checks {
        public static final HashSet<Material> armorMaterials = new HashSet<Material>() {{
            add(Material.CHAINMAIL_HELMET);
            add(Material.CHAINMAIL_CHESTPLATE);
            add(Material.CHAINMAIL_LEGGINGS);
            add(Material.CHAINMAIL_BOOTS);

            add(Material.IRON_HELMET);
            add(Material.IRON_CHESTPLATE);
            add(Material.IRON_LEGGINGS);
            add(Material.IRON_BOOTS);

            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_HELMET") : Material.GOLDEN_HELMET));
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_CHESTPLATE") : Material.GOLDEN_CHESTPLATE));
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_LEGGINGS") : Material.GOLDEN_LEGGINGS));
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_BOOTS") : Material.GOLDEN_BOOTS));

            add(Material.DIAMOND_HELMET);
            add(Material.DIAMOND_CHESTPLATE);
            add(Material.DIAMOND_LEGGINGS);
            add(Material.DIAMOND_BOOTS);

            add(Material.ELYTRA);
        }};

        public static final HashSet<Material> weaponMaterials = new HashSet<Material>() {{
            add((CompatUtil.is1_12() ? Material.getMaterial("WOOD_PICKAXE") : Material.WOODEN_PICKAXE));
            add(Material.STONE_PICKAXE);
            add(Material.IRON_PICKAXE);
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_PICKAXE") : Material.GOLDEN_PICKAXE));
//        add(Material.DIAMOND_PICKAXE);

            add((CompatUtil.is1_12() ? Material.getMaterial("WOOD_AXE") : Material.WOODEN_AXE));
            add(Material.STONE_AXE);
            add(Material.IRON_AXE);
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_AXE") : Material.GOLDEN_AXE));
            add(Material.DIAMOND_AXE);

            add((CompatUtil.is1_12() ? Material.getMaterial("WOOD_HOE") : Material.WOODEN_HOE));
            add(Material.STONE_HOE);
            add(Material.IRON_HOE);
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_HOE") : Material.GOLDEN_HOE));
            add(Material.DIAMOND_HOE);

            add((CompatUtil.is1_12() ? Material.getMaterial("WOOD_SWORD") : Material.WOODEN_SWORD));
            add(Material.STONE_SWORD);
            add(Material.IRON_SWORD);
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_SWORD") : Material.GOLDEN_SWORD));
            add(Material.DIAMOND_SWORD);

            add((CompatUtil.is1_12() ? Material.getMaterial("WOOD_SPADE") : Material.WOODEN_SHOVEL));
            add((CompatUtil.is1_12() ? Material.getMaterial("STONE_SPADE") : Material.STONE_SHOVEL));
            add((CompatUtil.is1_12() ? Material.getMaterial("IRON_SPADE") : Material.IRON_SHOVEL));
            add((CompatUtil.is1_12() ? Material.getMaterial("GOLD_SPADE") : Material.GOLDEN_SHOVEL));
            add((CompatUtil.is1_12() ? Material.getMaterial("DIAMOND_SPADE") : Material.DIAMOND_SHOVEL));

            add(Material.BOW);
        }};

        public static final HashSet<Material> illegalBlocks = new HashSet<Material>() {{
            add(Material.BEDROCK);
//            add(Material.ENDER_PORTAL_FRAME);
            add(Material.BARRIER);
            add(Material.STRUCTURE_BLOCK);
            add(Material.STRUCTURE_VOID);
//            add(Material.MOB_SPAWNER);
//            add(Material.MONSTER_EGG);
//            add(Material.COMMAND);
//            add(Material.COMMAND_CHAIN);
//            add(Material.COMMAND_MINECART);
//            add(Material.COMMAND_REPEATING);
        }};

        public static boolean isIllegalBlock(final Material material) {
            if (material == null) {
                return false;
            }

            return illegalBlocks.contains(material);
        }

        public static boolean isArmor(final ItemStack itemStack) {
            if (itemStack == null) {
                return false;
            }

            return armorMaterials.contains(itemStack.getType());
        }

        public static boolean isWeapon(final ItemStack itemStack) {
            if (itemStack == null) {
                return false;
            }

            return weaponMaterials.contains(itemStack.getType());
        }
    }

}
