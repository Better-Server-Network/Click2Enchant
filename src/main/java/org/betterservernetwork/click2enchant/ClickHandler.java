package org.betterservernetwork.click2enchant;

import org.betterservernetwork.click2enchant.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;

public class ClickHandler implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir() ||
                event.getClick() != ClickType.LEFT || event.getCursor().getType() != Material.ENCHANTED_BOOK ||
                !(event.getCursor().getItemMeta() instanceof EnchantmentStorageMeta) ||
                DisableCommandHandler.instance.isDisabled((Player) event.getWhoClicked())) return;

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) event.getCursor().getItemMeta();

        if (!meta.hasStoredEnchants()) return;

        event.setCancelled(true);

        if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK &&
                event.getCurrentItem().getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) event.getCurrentItem().getItemMeta();

            if (!meta2.hasStoredEnchants()) return;

            EnchantmentStorageMeta meta3 = null;

            for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                if (meta2.getStoredEnchants().get(enchantment) != null &&
                        meta.getStoredEnchants().get(enchantment) != null &&
                        meta2.getStoredEnchants().get(enchantment).intValue() ==
                                meta.getStoredEnchants().get(enchantment) &&
                        enchantment.getMaxLevel() > meta2.getStoredEnchants().get(enchantment)) {
                    meta2.removeStoredEnchant(enchantment);
                    meta2.addStoredEnchant(enchantment, meta.getStoredEnchants().get(enchantment) + 1, true);
                } else if (meta2.getStoredEnchants().get(enchantment) != null &&
                        meta.getStoredEnchants().get(enchantment) != null) {
                    if (meta3 == null) {
                        meta3 = (EnchantmentStorageMeta) (new ItemStack(Material.ENCHANTED_BOOK)).getItemMeta();
                    }
                    meta3.addStoredEnchant(enchantment, meta.getStoredEnchants().get(enchantment), true);
                } else {
                    meta2.addStoredEnchant(enchantment, meta.getStoredEnchants().get(enchantment), true);
                }
            }

            if (meta3 != null) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                item.setItemMeta(meta3);
                event.getWhoClicked().setItemOnCursor(item);
            } else {
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            }

            event.getCurrentItem().setItemMeta(meta2);

            return;
        }

        EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) event.getCursor().getItemMeta();
        ArrayList<Enchantment> list = new ArrayList<>();

        for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
            if (event.getCurrentItem().getEnchantments().get(enchantment) != null &&
                    meta.getStoredEnchants().get(enchantment) != null &&
                    event.getCurrentItem().getEnchantments().get(enchantment).intValue() ==
                            meta.getStoredEnchants().get(enchantment) &&
                    enchantment.getMaxLevel() > event.getCurrentItem().getEnchantments().get(enchantment)) {
                list.add(enchantment);
                meta2.removeStoredEnchant(enchantment);
                event.getCurrentItem().removeEnchantment(enchantment);
                event.getCurrentItem().addEnchantment(enchantment, meta.getStoredEnchants().get(enchantment) + 1);
            } else if (event.getCurrentItem().getEnchantments().get(enchantment) != null &&
                    meta.getStoredEnchants().get(enchantment) != null) {
                list.add(enchantment);
            }
        }

        loop:
        for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
            if (!enchantment.canEnchantItem(event.getCurrentItem()) || list.contains(enchantment)) {
                continue;
            }

            for (Enchantment enchantment2 : event.getCurrentItem().getEnchantments().keySet()) {
                if (enchantment.conflictsWith(enchantment2) && enchantment != enchantment2) {
                    continue loop;
                }
            }

            meta2.removeStoredEnchant(enchantment);
            event.getCurrentItem().addEnchantment(enchantment, meta.getStoredEnchants().get(enchantment));
        }

        if (meta2.hasStoredEnchants()) {
            event.getCursor().setItemMeta(meta2);
        } else {
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onInventoryRightClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getClick() != ClickType.RIGHT ||
                event.getCurrentItem().getType().isAir() || event.getCurrentItem().getEnchantments().isEmpty() ||
                DisableCommandHandler.instance.isDisabled((Player) event.getWhoClicked())) return;

        event.setCancelled(true);

        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        if (Tools.playerHasSpace((Player) event.getWhoClicked(), item)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

            for (Enchantment enchantment : event.getCurrentItem().getEnchantments().keySet()) {
                meta.addStoredEnchant(enchantment, event.getCurrentItem().getEnchantments().get(enchantment), true);
            }

            item.setItemMeta(meta);
            event.getWhoClicked().getInventory().addItem(item);

            for (Enchantment enchantment : event.getCurrentItem().getEnchantments().keySet()) {
                event.getCurrentItem().removeEnchantment(enchantment);
            }
        } else {
            event.getWhoClicked().sendMessage(ChatColor.RED + "You do not have enough space.");
        }
    }

    @EventHandler
    public void onInventoryRightClickBook(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getClick() != ClickType.RIGHT ||
                event.getCurrentItem().getType().isAir() ||
                event.getCurrentItem().getType() != Material.ENCHANTED_BOOK ||
                DisableCommandHandler.instance.isDisabled((Player) event.getWhoClicked())) return;

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) event.getCurrentItem().getItemMeta();
        if (!meta.hasStoredEnchants()) return;

        event.setCancelled(true);

        Enchantment firstEnchantment = (Enchantment) meta.getStoredEnchants().keySet().toArray()[0];
        int firstLevel = meta.getStoredEnchants().get(firstEnchantment);

        if (meta.getStoredEnchants().size() > 1) {
            if (playerHasSpace((Player) event.getWhoClicked(), meta.getStoredEnchants().size())) {
                for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                    event.getWhoClicked().getInventory().addItem(getEnchantedBook(enchantment,
                            meta.getStoredEnchants().get(enchantment)));
                }

                event.setCurrentItem(new ItemStack(Material.AIR));
            } else {
                event.getWhoClicked().sendMessage(ChatColor.RED + "You do not have enough space.");
            }
        } else if (firstLevel > 1) {
            if (playerHasSpace((Player) event.getWhoClicked(), 2)) {
                event.getWhoClicked().getInventory().addItem(
                        getEnchantedBook(firstEnchantment, firstLevel-1),
                        getEnchantedBook(firstEnchantment, firstLevel-1));

                event.setCurrentItem(new ItemStack(Material.AIR));
            } else {
                event.getWhoClicked().sendMessage(ChatColor.RED + "You do not have enough space.");
            }
        }
    }

    public static boolean playerHasSpace(Player player, int items) {
        int freeSlots = 0;
        int maxStackSize = 1;

        for (ItemStack invItem : player.getInventory().getStorageContents()) {
            if (invItem == null) {
                freeSlots += maxStackSize;
            }
        }

        return freeSlots >= items;
    }

    private ItemStack getEnchantedBook(Enchantment enchantment, int level) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return item;
    }
}
