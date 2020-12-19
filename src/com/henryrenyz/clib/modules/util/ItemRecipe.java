package com.henryrenyz.clib.modules.util;

import com.henryrenyz.clib.modules.nbt.NBTCompound;
import com.henryrenyz.clib.modules.nbt.NBTItem;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ItemRecipe<T extends ItemRecipe.Matcher> {

    private final T[] input;
    private final ItemStack[] output;

    public ItemRecipe(List<T> input, List<ItemStack> output) {
        this.input = (T[]) input.toArray();
        this.output = output.toArray(new ItemStack[0]);
    }

    public ItemRecipe(Matcher[] input, ItemStack[] output) {
        this.input = (T[]) input;
        this.output = output;
    }

    public ItemRecipe(Matcher input, ItemStack output) {
        this.input = (T[]) new Matcher[]{input};
        this.output = new ItemStack[]{output};
    }

    public boolean match(List<ItemStack> items) {
        for (Matcher c : input) {
            boolean b = false;
            for (ItemStack i : items) {
                if (c.match(i)) b = true;
            }
            if (!b) return false;
        }
        return true;
    }

    public T[] getInput() {
        return input;
    }

    public ItemStack[] getOutput() {
        return output;
    }

    public static class Matcher {
        protected NBTCompound element;
        protected NBTCompound without;
        protected Material mat;
        protected int amount;

        public Matcher(@Nullable Material mat, int amount, String rawNBT, String without) throws Exception {
            this(mat, amount, new NBTCompound(rawNBT), new NBTCompound(without));
        }

        public Matcher(@Nullable Material mat, int amount, NBTCompound compound, NBTCompound without) {
            this.mat = mat;
            this.element = compound;
            this.without = without;
            this.amount = amount;
        }

        public Matcher(Material mat, int amount, String rawNBT) throws Exception {
            this(mat, amount, new NBTCompound(rawNBT), null);
        }

        public Matcher(@Nullable Material mat, int amount) {
            this(mat, amount, (NBTCompound) null, null);
        }

        public Matcher(@Nullable Material mat) {
            this(mat, 1);
        }

        public boolean match(NBTItem item) {
            if (item.getAmount() >= amount) {
                if (mat != null) {
                    if (item.getMaterial() == mat && element != null && item.contains(element)) {
                        if (without != null) {
                            return !item.contains(without);
                        } else return true;
                    }
                } else {
                    if (element != null && item.contains(element)) {
                        if (without != null) {
                            return !item.contains(without);
                        } else return true;
                    }
                }
            }
            return false;
        }

        public boolean match(ItemStack item) {
            return match(new NBTItem(item));
        }
    }
}
