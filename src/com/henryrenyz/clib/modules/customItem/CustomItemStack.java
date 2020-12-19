package com.henryrenyz.clib.modules.customItem;

import com.henryrenyz.clib.modules.nbt.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomItemStack {

    protected CustomItem mat;

    protected NBTItem item;

    protected List<DynamicLore> dlores = new ArrayList<>();

    public CustomItemStack(CustomItem mat, ItemStack item) {
        this.mat = mat;
        this.item = new NBTItem(item);
    }

    public CustomItemStack(String mat, ItemStack item) {
        if (CustomItem.getRegistered().get(mat) != null) {
            this.mat = CustomItem.getRegistered().get(mat);
        } else this.mat = new CustomItem(mat);
        this.item = new NBTItem(item);
    }

    public CustomItem getMat() {
        return mat;
    }

    public NBTItem getItem() {
        return item;
    }

    public List<DynamicLore> getLore() {
        return dlores;
    }

    public void setLore(List<DynamicLore> lore) {
        this.dlores = lore;
    }

    public static CustomItemStack get(ItemStack item) {
        NBTItem i = new NBTItem(item);
        if (i.hasKey("CustomItem")) {
            String tag = i.getString("CustomItem");
            CustomItem mat = CustomItem.getRegistered().get(tag);
            if (tag != null && mat != null) {
                return new CustomItemStack(mat, item);
            }
            return new CustomItemStack(new CustomItem(tag), item);
        }
        return null;
    }

    public String toString() {
        return "CustomItemStack{name: \"" + mat.getName() + "\"}";
    }
}
