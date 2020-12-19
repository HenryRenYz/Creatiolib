package com.henryrenyz.clib.modules.customItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemManager {

    private static final Map<Player, Map<Integer, CustomItemStack>> SERVER_ITEM = new HashMap<>();

    private static final Runnable TASK_UPDATE_ALL = () -> {
        for (Player p : Bukkit.getOnlinePlayers()) {
            updateInv(p);
        }
    };

    //No default constructor
    private ItemManager() {}



    public static Collection<CustomItemStack> getOwnerItem(Player player) {
        return SERVER_ITEM.get(player).values();
    }

    public static Map<Integer, CustomItemStack> getOwnerMap(Player player) {
        return SERVER_ITEM.get(player);
    }

    public static Map<Player, Map<Integer, CustomItemStack>> getAllMap() {
        return SERVER_ITEM;
    }



    public static void updateAll() {
        new Thread(TASK_UPDATE_ALL).start();
    }

    public static void updateInv(Player player) {
        SERVER_ITEM.computeIfAbsent(player, k -> new HashMap<>());
        SERVER_ITEM.get(player).clear();
        ItemStack[] items = player.getInventory().getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) updateItem(player, items[i], i);
        }
    }

    public static void updateItems(Player player, int... slot) {
        for (int i : slot) {
            updateItem(player, player.getInventory().getItem(i), i);
        }
    }

    public static void updateItem(Player player, ItemStack item, int slot) {
        if (SERVER_ITEM.containsKey(player)) SERVER_ITEM.get(player).remove(slot);
        if (item != null) {
            if (item.getItemMeta() != null) {
                if (item.getItemMeta().hasCustomModelData()) {
                    CustomItemStack c = CustomItemStack.get(item);
                    if (c != null) {
                        SERVER_ITEM.computeIfAbsent(player, k -> new HashMap<>());
                        SERVER_ITEM.get(player).put(slot, c);
                    }
                }
            }
        }
    }

    public static void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv != null) {
            if (inv.getType() == InventoryType.PLAYER) {
                ItemStack i = e.getClickedInventory().getItem(e.getSlot());
                switch (e.getAction()) {
                    case PLACE_ONE:
                    case PLACE_SOME:
                    case PLACE_ALL:
                    case SWAP_WITH_CURSOR:
                    case HOTBAR_SWAP:
                    case MOVE_TO_OTHER_INVENTORY:
                        if (i != null) updateItem((Player) e.getWhoClicked(), i, e.getSlot());
                        if (e.getCursor() != null) updateItem((Player) e.getWhoClicked(), e.getCursor(), -1);
                }
            }
        }
    }

    public static void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getType() == InventoryType.PLAYER) {
            for (int i : e.getInventorySlots()) {
                ItemStack it = e.getInventory().getItem(i);
                if (it != null) updateItem((Player) e.getWhoClicked(), it, i);
            }
        }
    }
}
