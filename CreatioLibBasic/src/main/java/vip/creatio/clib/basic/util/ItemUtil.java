package vip.creatio.clib.basic.util;

import com.google.common.collect.Multimap;
import vip.creatio.clib.basic.chat.Component;
import vip.creatio.clib.basic.nbt.*;
import vip.creatio.accessor.Func;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemAxe;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.clib.basic.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public final class ItemUtil {

    public static final ItemStack NULL = new ItemStack(Material.AIR);

    //No default constructor
    private ItemUtil() {}

    private static final Var<ItemMeta> ITEM_META_VAR = Reflection.field(ItemStack.class, "meta");
    public static ItemMeta getMeta(@NotNull ItemStack item) {
        ItemMeta meta = ITEM_META_VAR.get(item);
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(meta);
        }
        return meta;
    }

    public static void setName(@NotNull ItemStack item, String name) {
        getMeta(item).setDisplayName(name);
    }

    public static void setLores(@NotNull ItemStack item, List<String> lores) {
        getMeta(item).setLore(lores);
    }

    public static void setLore(@NotNull ItemStack item, String lore, int row) {
        ItemMeta m = getMeta(item);
        List<String> lores = m.getLore();
        lores = lores == null ? new ArrayList<>() : lores;
        if (lores.size() < row + 1) {
            for (int i = 0; i < row + 1; i++) {
                lores.add("§5");
            }
        }
        lores.set(row, lore);
        m.setLore(lores);
    }

    public static void setModelData(@NotNull ItemStack item, Integer data) {
        getMeta(item).setCustomModelData(data);
    }

    public static void addEnchant(@NotNull ItemStack item, Enchantment enchantment, int i, boolean b) {
        getMeta(item).addEnchant(enchantment, i, b);
    }

    public static void removeEnchant(@NotNull ItemStack item, Enchantment enchantment) {
        getMeta(item).removeEnchant(enchantment);
    }

    public static void addFlags(@NotNull ItemStack item, ItemFlag... var1) {
        getMeta(item).addItemFlags(var1);
    }

    public static void addAttribute(@NotNull ItemStack item, Attribute attribute, AttributeModifier modifier) {
        getMeta(item).addAttributeModifier(attribute, modifier);
    }

    public static void setAttribute(@NotNull ItemStack item, Multimap<Attribute, AttributeModifier> map) {
        getMeta(item).setAttributeModifiers(map);
    }

    public static ItemStack toBukkitCopy(@NotNull net.minecraft.server.ItemStack nmsItem) {
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private static final Func<CraftItemStack> CRAFT_ITEM_STACK = Reflection.constructor(CraftItemStack.class, net.minecraft.server.ItemStack.class);
    public static ItemStack toBukkit(@NotNull net.minecraft.server.ItemStack nmsItem) {
        return CRAFT_ITEM_STACK.invoke(nmsItem);
    }

    private static final Var<net.minecraft.server.ItemStack> ITEM_STACK_HANDLE_VAR = Reflection.field(CraftItemStack.class, "handle");
    public static net.minecraft.server.ItemStack toNms(@NotNull ItemStack item) {
        return ITEM_STACK_HANDLE_VAR.get(item);
    }

    public static net.minecraft.server.ItemStack toNmsCopy(@NotNull ItemStack item) {
        return CraftItemStack.asNMSCopy(item);
    }

    public static Item toNms(@NotNull Material mat) {
        return CraftMagicNumbers.getItem(mat);
    }

    public static Material toBukkit(@NotNull Item nmsMat) {
        return CraftMagicNumbers.getMaterial(nmsMat);
    }

    public static boolean isFireResistance(@NotNull Material mat) {
        return toNms(mat).u(); /* isItemFireResistance */
    }

    public static boolean isArmor(@NotNull Material mat) {
        return toNms(mat) instanceof ItemArmor;
    }

    public static int getMaxDurability(@NotNull Material mat) {
        return toNms(mat).getMaxDurability();
    }
    public static int getMaxDurability(@NotNull ItemStack item) {
        return toNms(item).getItem().getMaxDurability();
    }

    public static int getMaxStackSize(@NotNull Material mat) {
        return toNms(mat).getMaxStackSize();
    }

    public static boolean isFood(@NotNull Material mat) {
        return toNms(mat).isFood();
    }

    public static boolean isAxe(@NotNull Material mat) {
        return toNms(mat) instanceof ItemAxe;
    }

    public static <T extends LivingEntity> void damage(@NotNull ItemStack item, int amount, T entity, Consumer<T> onBreak) {
        ItemMeta meta = getMeta(item);
        if (meta == null) return;
        if ((!(entity instanceof Player) || !EntityUtil.INVULNERABLE_MODE.contains(((Player) entity).getGameMode()))
                && meta.isUnbreakable()
                && damageItem(item,
                             amount,
                             EntityUtil.getRandom(entity),
                             entity instanceof Player ? (Player) entity : null)) {
            onBreak.accept(entity);

            if (item.getAmount() == 1 && entity instanceof Player) {
                Bukkit.getPluginManager().callEvent(new PlayerItemBreakEvent((Player) entity, item));
            }

            ((Damageable) item).setDamage(0);
        }
    }

    public static boolean damageItem(ItemStack item, int amount, Random random, @Nullable Player player) {
        if (!((Damageable) item).hasDamage()) {
            return false;
        } else {
            int level;
            if (amount > 0) {
                level = item.getEnchantmentLevel(Enchantment.DURABILITY);
                int k = 0;

                for(int l = 0; level > 0 && l < amount; ++l) {
                    boolean bool;
                    if (isArmor(item.getType()) && random.nextFloat() < 0.6F) {
                        bool = false;
                    } else {
                        bool = random.nextInt(amount + 1) > 0;
                    }
                    if (bool) {
                        ++k;
                    }
                }

                amount -= k;
                if (player != null) {
                    PlayerItemDamageEvent event = new PlayerItemDamageEvent(player, item, amount);
                    Bukkit.getPluginManager().callEvent(event);
                    if (amount != event.getDamage() || event.isCancelled()) {
                        event.getPlayer().updateInventory();
                    }

                    if (event.isCancelled()) {
                        return false;
                    }

                    amount = event.getDamage();
                }

                if (amount <= 0) {
                    return false;
                }
            }

            level = ((Damageable)item).getDamage() + amount;
            ((Damageable)item).setDamage(level);
            return level >= getMaxDurability(item);
        }
    }

    public static boolean isEmpty(ItemStack item) {
        return item.equals(NULL) || item.getType() == Material.AIR || item.getAmount() <= 0;
    }

    public static CompoundTag getOrCreateTag(ItemStack itemStack) {
        net.minecraft.server.ItemStack nms = toNms(itemStack);
        return getOrCreateTag(nms);
    }

    public static @Nullable CompoundTag getTag(ItemStack itemStack) {
        net.minecraft.server.ItemStack nms = toNms(itemStack);
        return getTag(nms);
    }

    public static CompoundTag getOrCreateTag(net.minecraft.server.ItemStack nms) {
        return new CompoundTag(nms.getOrCreateTag());
    }

    public static @Nullable CompoundTag getTag(net.minecraft.server.ItemStack nms) {
        NBTTagCompound nmsTag = nms.getTag();
        return nmsTag == null ? null : new CompoundTag(nms.getTag());
    }

    public static List<net.minecraft.server.ItemStack> getNmsInvItems(Player p) {
        return EntityUtil.toNms(p).inventory.items;
    }

    public static @Nullable List<Component> getLores(net.minecraft.server.ItemStack nms) {
        CompoundTag tag = getTag(nms);
        if (tag == null) return null;

        CompoundTag display = (CompoundTag) tag.get("display");
        if (display == null) return null;

        ListTag<?> lores = (ListTag<?>) display.get("Lore");
        if (lores == null || lores.getElementType() != NBTType.STRING)
            return null;

        List<Component> comp = new ArrayList<>();
        for (NBTTag<?> t : lores) {
            Component c = Component.fromJson(((StringTag) t).asString());
            if (c != null) comp.add(c);
        }
        return comp;
    }

    public static @Nullable List<Component> getLores(ItemStack itemStack) {
        return getLores(toNms(itemStack));
    }

    public static ItemStack fromNBT(CompoundTag tag) {
        return toBukkit(net.minecraft.server.ItemStack.a /* fromNBT */ (tag.unwrap()));
    }

    public static ItemStack fromNBT(Material mat, int count, CompoundTag tag) {
        CompoundTag item = new CompoundTag();
        item.putByte("Count", (byte) count);
        item.putString("id", toNms(mat).getName());
        item.put("tag", tag);
        return fromNBT(item);
    }
}
