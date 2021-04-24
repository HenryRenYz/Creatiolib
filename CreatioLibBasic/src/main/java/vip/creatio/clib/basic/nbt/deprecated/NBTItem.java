package vip.creatio.clib.basic.nbt.deprecated;

import com.henryrenyz.clib.basic.nbt.CompoundTag;
import com.henryrenyz.clib.basic.nbt.ListTag;
import com.henryrenyz.clib.basic.nbt.NBTType;
import com.henryrenyz.clib.modules.util.ItemRecipe;
import net.minecraft.server.NBTTagCompound;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import vip.creatio.clib.basic.nbt.CompoundTag;
import vip.creatio.clib.basic.nbt.ListTag;
import vip.creatio.clib.basic.nbt.NBTType;

import java.util.HashMap;
import java.util.Map;

/** Should have a full-functioning Itemstack wrapper replacing this */
@Deprecated
public class NBTItem extends CompoundTag {

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
        super.setCompound(CraftItemStack.asNMSCopy(itemStack).getTag());
    }

    public NBTItem(@NotNull String nbt) throws Exception {
        super(nbt);
        this.amount = 1;
        this.material = Material.AIR;
    }

    public NBTItem(@NotNull CompoundTag compound) {
        super(compound.getCompound("tag"));
        this.amount = compound.getByte("Count");
        //this.material = BukkitUtil.getMaterial(compound.getString("id"));
    }

    public ItemStack getItemStack() {
        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.ItemStack.a(getItemNBT().getCompound()));
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
    public CompoundTag getItemNBT() {
        CompoundTag c = new CompoundTag();
        c.putString("id", material.getKey().getKey()).putByte("Count", (byte) amount).put("tag", this);
        return c;
    }
     public NBTItem setItemNBT(CompoundTag compound) {
        setCompound(compound.getCompound());
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
        putInt("Damage", damage);
        return this;
    }

    //Enchantments
        //Contains enchantment
    public boolean containsEnchantment(@NotNull Enchantment ench) {
        ListTag<NBTTagCompound> l = (ListTag<NBTTagCompound>) getList("Enchantments", NBTType.COMPOUND);

        for (int i = 0; i < l.size(); i++) {
            if (l.getCompound(i).getString("id").equalsIgnoreCase(ench.getKey().toString())) return true;
        }
        return false;
    }
        //Get enchantment level
    public int getEnchantmentLevel(@NotNull Enchantment ench) {
        ListTag<NBTTagCompound> l = (ListTag<NBTTagCompound>) getList("Enchantments", NBTType.COMPOUND);

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
        ListTag<NBTTagCompound> l = (ListTag<NBTTagCompound>) getList("Enchantments", NBTType.COMPOUND);

        for (int i = 0; i < l.size(); i++) {
            Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(l.getCompound(i).getString("id").split(":")[1]));
            if (e != null) map.put(e, (int) l.getCompound(i).getShort("lvl"));
        }
        return map;
    }
        //Add enchantments using a map
    public NBTItem addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, "Enchantment cannot be null");

        for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
            addEnchantment(e.getKey(), e.getValue());
        }
        return this;
    }
        //Add single enchantment
    public NBTItem addEnchantment(@NotNull Enchantment enchantment, int level) {
        ListTag<NBTTagCompound> l = (ListTag<NBTTagCompound>) getList("Enchantments", NBTType.COMPOUND);
        CompoundTag c = new CompoundTag().putString("id", enchantment.getKey().toString()).putShort("lvl", (short) level);
        l.add(c);
        return this;
    }
        //Remove single enchantment, returns the level of removed enchantment(remove the first enchantment iterator meets)
    public int removeEnchantment(@NotNull Enchantment enchantment) {
        ListTag<NBTTagCompound> l = (ListTag<NBTTagCompound>) getList("Enchantments", NBTType.COMPOUND);
        
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
        ListTag<NBTTagCompound> l = (ListTag<NBTTagCompound>) getList("Enchantments", NBTType.COMPOUND);

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
}
