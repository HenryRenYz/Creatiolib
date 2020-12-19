package com.henryrenyz.clib.modules.util;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ItemUtil extends ItemStack {

    public ItemUtil setName(String name) {
        ItemMeta m = super.getItemMeta();
        m.setDisplayName(name);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil setLores(List<String> lores) {
        ItemMeta m = super.getItemMeta();
        m.setLore(lores);
        return this;
    }

    public ItemUtil setLore(String lore, int row) {
        ItemMeta m = super.getItemMeta();
        List<String> lores = m.getLore();
        lores = lores == null ? new ArrayList<>() : lores;
        if (lores.size() < row + 1) {
            for (int i = 0; i < row + 1; i++) {
                lores.add("§5");
            }
        }
        lores.set(row, lore);
        m.setLore(lores);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil setModelData(Integer data) {
        ItemMeta m = super.getItemMeta();
        m.setCustomModelData(data);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil addEnchant(Enchantment enchantment, int i, boolean b) {
        ItemMeta m = super.getItemMeta();
        m.addEnchant(enchantment, i, b);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil removeEnchant(Enchantment enchantment) {
        ItemMeta m = super.getItemMeta();
        m.removeEnchant(enchantment);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil addFlags(ItemFlag... var1) {
        ItemMeta m = super.getItemMeta();
        m.addItemFlags(var1);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil addAttribute(Attribute attribute, AttributeModifier modifier) {
        ItemMeta m = super.getItemMeta();
        m.addAttributeModifier(attribute, modifier);
        super.setItemMeta(m);
        return this;
    }

    public ItemUtil setAttribute(Multimap<Attribute, AttributeModifier> map) {
        ItemMeta m = super.getItemMeta();
        m.setAttributeModifiers(map);
        super.setItemMeta(m);
        return this;
    }

}
