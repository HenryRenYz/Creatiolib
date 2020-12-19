package com.henryrenyz.clib.modules.customItem;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Item which contains "CustomItem" tag.
 */
public class CustomItem {

    private String name;

    private static final Map<String, CustomItem> REGISTERED = new HashMap<>();

    public CustomItem(String name) {
        this.name = name;
        REGISTERED.put(name, this);
    }

    public String getName() {
        return name;
    }

    public static Map<String, CustomItem> getRegistered() {
        return REGISTERED;
    }


}
