package vip.creatio.clib.basic.nbt.deprecated;

import com.henryrenyz.clib.access.util.nbt.*;
import com.henryrenyz.clib.basic.nbt.CompoundTag;
import com.henryrenyz.clib.basic.nbt.LongArrayTag;
import com.henryrenyz.clib.util.BukkitUtil;
import com.henryrenyz.clib.util.ItemUtil;
import net.minecraft.server.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.creatio.clib.basic.nbt.CompoundTag;
import vip.creatio.clib.basic.nbt.LongArrayTag;

import java.util.Collection;
import java.util.List;

public class NBTAccessImpl implements NBTAccess {

    // NBT Array
    @Override
    public CraftByteArrayTag newByteArray(byte[] array) {
        return new CraftByteArrayTag(array);
    }

    @Override
    public CraftByteArrayTag newByteArray(List<Byte> list) {
        return new CraftByteArrayTag(list);
    }

    @Override
    public CraftIntArrayTag newIntArray(int[] array) {
        return new CraftIntArrayTag(array);
    }

    @Override
    public CraftIntArrayTag newIntArray(List<Integer> list) {
        return new CraftIntArrayTag(list);
    }

    @Override
    public CraftLongArrayTag newLongArray(long[] array) {
        return new CraftLongArrayTag(array);
    }

    @Override
    public CraftLongArrayTag newLongArray(List<Long> list) {
        return new CraftLongArrayTag(list);
    }


    // Compound
    @Override
    public CraftCompoundTag newCompound(@NotNull String nbt) {
        return new CraftCompoundTag(nbt);
    }

    @Override
    public CraftCompoundTag newCompound() {
        return new CraftCompoundTag();
    }


    // String
    @Override
    public CraftStringTag stringOf(String str) {
        return new CraftStringTag(str);
    }


    // NBT Number
    @Override
    public CraftByteTag valueOf(byte value) {
        return CraftByteTag.valueOf(value);
    }

    @Override
    public CraftDoubleTag valueOf(double value) {
        return CraftDoubleTag.valueOf(value);
    }

    @Override
    public CraftFloatTag valueOf(float value) {
        return CraftFloatTag.valueOf(value);
    }

    @Override
    public CraftShortTag valueOf(short value) {
        return CraftShortTag.valueOf(value);
    }

    @Override
    public CraftIntTag valueOf(int value) {
        return CraftIntTag.valueOf(value);
    }

    @Override
    public CraftLongTag valueOf(long value) {
        return CraftLongTag.valueOf(value);
    }


    // List
    @Override
    @SuppressWarnings("unchecked")
    public <T extends NBTTag> ListTag<T> listOf(T... tags) {
        return (ListTag<T>) new CraftListTag<>((CraftNBTTag<NBTBase>[]) tags);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends NBTTag> ListTag<T> listOf(Collection<T> tags) {
        return (ListTag<T>) new CraftListTag<>((CraftListTag<NBTBase>) tags);
    }


    // Clone
    @Override
    public CraftByteArrayTag clone(ByteArrayTag array) {
        return new CraftByteArrayTag((CraftByteArrayTag) array);
    }

    @Override
    public CraftIntArrayTag clone(IntArrayTag array) {
        return new CraftIntArrayTag((CraftIntArrayTag) array);
    }

    @Override
    public CraftLongArrayTag clone(LongArrayTag array) {
        return new CraftLongArrayTag((CraftLongArrayTag) array);
    }

    @Override
    public CraftCompoundTag clone(CompoundTag compound) {
        return new CraftCompoundTag((CraftCompoundTag) compound);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends NBTTag> ListTag<T> clone(ListTag<T> list) {
        return (ListTag<T>) new CraftListTag<>((CraftListTag<NBTBase>) list);
    }


    // Utilities
    @Override
    public @Nullable CraftCompoundTag getTag(org.bukkit.inventory.ItemStack itemStack) {
        return ItemUtil.getTag(itemStack);
    }

    @Override
    public @NotNull CraftCompoundTag getOrCreateTag(org.bukkit.inventory.ItemStack itemStack) {
        return ItemUtil.getOrCreateTag(itemStack);
    }

    @Override
    public @NotNull CraftCompoundTag parse(String nbt) {
        return BukkitUtil.parseNbt(nbt);
    }
}
