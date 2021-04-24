package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagIntArray;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IntArrayTag
extends CollectionTag<IntTag> {

    public IntArrayTag(int[] array) {
        super(new NBTTagIntArray(array));
    }

    public IntArrayTag(List<Integer> array) {
        super(new NBTTagIntArray(array));
    }

    public IntArrayTag(NBTTagIntArray tagIntArray) {
        super(tagIntArray);
    }

    public IntArrayTag(@NotNull IntArrayTag clone) {
        super((NBTTagIntArray) clone.list.clone());
    }


    @Override
    public NBTType getType() {
        return NBTType.INTARRAY;
    }

    @Override
    public NBTType getElementType() {
        return NBTType.INT;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IntArrayTag)
            return list.equals(((IntArrayTag) obj).list);
        return false;
    }

    @Override
    public IntTag remove(int index) {
        return NBTTag.wrap(list.remove(index));
    }

    @Override
    public IntTag set(int index, IntTag item) {
        return NBTTag.wrap(list.set(index, item.unwrap()));
    }

    @Override
    public IntTag get(int index) {
        return NBTTag.wrap(list.get(index));
    }

    @Override
    public NBTTagIntArray unwrap() {
        return (NBTTagIntArray) super.unwrap();
    }

    public int[] getInts() {
        return unwrap().getInts();
    }

    public void add(int index, int i) {
        super.add(index, IntTag.valueOf(i));
    }

    public void add(int i) {
        super.add(IntTag.valueOf(i));
    }

    public void set(int index, int i) {
        set(index, IntTag.valueOf(i));
    }
}
