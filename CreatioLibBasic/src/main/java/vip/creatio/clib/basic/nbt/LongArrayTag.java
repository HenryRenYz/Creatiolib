package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagLongArray;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LongArrayTag
extends CollectionTag<LongTag> {

    public LongArrayTag(long[] array) {
        super(new NBTTagLongArray(array));
    }

    public LongArrayTag(List<Long> array) {
        super(new NBTTagLongArray(array));
    }

    public LongArrayTag(NBTTagLongArray tagIntArray) {
        super(tagIntArray);
    }

    public LongArrayTag(@NotNull LongArrayTag clone) {
        super((NBTTagLongArray) clone.list.clone());
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
        if (obj instanceof LongArrayTag)
            return list.equals(((LongArrayTag) obj).list);
        return false;
    }

    @Override
    public LongTag remove(int index) {
        return NBTTag.wrap(list.remove(index));
    }

    @Override
    public LongTag set(int index, LongTag item) {
        return NBTTag.wrap(list.set(index, item.unwrap()));
    }

    @Override
    public LongTag get(int index) {
        return NBTTag.wrap(list.get(index));
    }

    @Override
    public NBTTagLongArray unwrap() {
        return (NBTTagLongArray) super.unwrap();
    }

    public long[] getLongs() {
        return unwrap().getLongs();
    }

    public void add(int index, long i) {
        super.add(index, LongTag.valueOf(i));
    }

    public void add(long i) {
        super.add(LongTag.valueOf(i));
    }

    public void set(int index, long i) {
        set(index, LongTag.valueOf(i));
    }
}
