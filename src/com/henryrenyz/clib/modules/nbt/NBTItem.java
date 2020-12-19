package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.rawText.Json;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;
import com.henryrenyz.clib.modules.reflection.ReflectionUtils;
import com.henryrenyz.clib.modules.util.ItemRecipe;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class NBTItem extends NBTCompound {

    private Material material;
    private int amount;

    public NBTItem() {
        super();
        this.amount = 1;
        this.material = Material.AIR;
    }

    public NBTItem(@NotNull Material type) {
        super();
        this.amount = 1;
        this.material = type;
    }

    public NBTItem(@NotNull Material type, int amount) {
        super();
        this.amount = amount;
        this.material = type;
    }

    public NBTItem(@NotNull ItemStack itemStack) {
        super();
        Validate.notNull(itemStack, "ItemStack cannot be null");
        Validate.notNull(itemStack.getItemMeta(), "This ItemStack does not have ItemMeta!");

        this.amount = itemStack.getAmount();
        this.material = itemStack.getType();
        try {
            super.setCompound(ReflectionMethod.ItemStack_getTag.m.invoke(ReflectionUtils.getNMSItemStack(itemStack)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public NBTItem(@NotNull String nbt) throws Exception {
        super(nbt);
    }

    public NBTItem(@NotNull NBTCompound compound) {
        super(compound.get("tag").getRaw());
        this.amount = compound.getByte("Count");
        this.material = ReflectionUtils.getMaterial(compound.getString("id"));
    }

    public ItemStack getItemStack() {
        try {
            return (ItemStack) ReflectionUtils.getBukkitItemStack(ReflectionUtils.getNMSItemStack(getItemNBT().getRaw()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Item type
    public Material getMaterial() {
        return this.material;
    }
    public NBTItem setMaterial(@NotNull Material type) {
        this.material = type;
        return this;
    }

    //Item NBT
    public NBTCompound getItemNBT() {
        NBTCompound c = new NBTCompound();
        c.setString("id", material.getKey().getKey()).setByte("Count", (byte) amount).set("tag", this);
        return c;
    }
     public NBTItem setItemNBT(NBTCompound compound) {
        compound = new NBTCompound(compound.getRaw());
        return this;
     }

    //Amount
    public int getAmount() {
        return amount;
    }
    public NBTItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    //Damage
    public int getDamage() {
        return getInt("Damage");
    }
    public NBTItem setDamage(int damage) {
        setInt("Damage", damage);
        return this;
    }

    //Enchantments
        //Contains enchantment
    public boolean containsEnchantment(@NotNull Enchantment ench) {
        NBTList l = getList("Enchantments", NBTType.COMPOUND);

        for (int i = 0; i < l.size(); i++) {
            if (l.getCompound(i).getString("id").equalsIgnoreCase(ench.getKey().toString())) return true;
        }
        return false;
    }
        //Get enchantment level
    public int getEnchantmentLevel(@NotNull Enchantment ench) {
        NBTList l = getList("Enchantments", NBTType.COMPOUND);

        for (int i = 0; i < l.size(); i++) {
            if (l.getCompound(i).getString("id").equalsIgnoreCase(ench.getKey().toString())) {
                return l.getCompound(i).getShort("lvl");
            }
        }
        return 0;
    }
        //Get all enchantments as a map
    public Map<Enchantment, Integer> getEnchantments() {
        Map<Enchantment, Integer> map = new HashMap<>();
        NBTList l = getList("Enchantments", NBTType.COMPOUND);

        for (int i = 0; i < l.size(); i++) {
            Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(l.getCompound(i).getString("id").split(":")[1]));
            if (e != null) map.put(e, (int) l.getCompound(i).getShort("lvl"));
        }
        return map;
    }
        //Add enchantments using a map
    public NBTItem addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, "Enchantment cannot be null");

        for (Map.Entry<Enchantment, Integer> e : enchantments.entrySet()) {
            addEnchantment(e.getKey(), e.getValue());
        }
        return this;
    }
        //Add single enchantment
    public NBTItem addEnchantment(@NotNull Enchantment enchantment, int level) {
        NBTList l = getList("Enchantments", NBTType.COMPOUND);
        NBTCompound c = new NBTCompound().setString("id", enchantment.getKey().toString()).setShort("lvl", (short) level);
        l.add(c);
        return this;
    }
        //Remove single enchantment, returns the level of removed enchantment(remove the first enchantment iterator meets)
    public int removeEnchantment(@NotNull Enchantment enchantment) {
        NBTList l = getList("Enchantments", NBTType.COMPOUND);
        
        for (int i = 0; i < l.size(); i++) {
            if (l.getCompound(i).getString("id").equalsIgnoreCase(enchantment.getKey().toString())) {
                int lvl = l.getCompound(i).getShort("lvl");
                l.remove(i);
                return lvl;
            }
        }
        return 0;
    }
        //Remove single enchantment with specific level
    public void removeEnchantment(@NotNull Enchantment enchantment, int level) {
        NBTList l = getList("Enchantments", NBTType.COMPOUND);

        for (int i = 0; i < l.size(); i++) {
            if (l.getCompound(i).getString("id").equalsIgnoreCase(enchantment.getKey().toString())) {
                l.remove(i);
                return;
            }
        }
    }

    public boolean match(ItemRecipe.Matcher map) {
        return map.match(this);
    }

    //Display name
    public Json getDisplayName() {
        NBTCompound d = getCompound("display");
        return Json.fromJSON(d.getString("Name"));
    }
}
