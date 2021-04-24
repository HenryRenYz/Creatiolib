package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagByteArray;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ByteArrayTag
extends CollectionTag<ByteTag> {

    public ByteArrayTag(byte[] array) {
        super(new NBTTagByteArray(array));
    }

    public ByteArrayTag(List<Byte> array) {
        super(new NBTTagByteArray(array));
    }

    public ByteArrayTag(NBTTagByteArray tagByteArray) {
        super(tagByteArray);
    }

    public ByteArrayTag(@NotNull ByteArrayTag clone) {
        super((NBTTagByteArray) clone.list.clone());
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTEARRAY;
    }

    @Override
    public NBTType getElementType() {
        return NBTType.BYTE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ByteArrayTag) {
            return list.equals(((ByteArrayTag) obj).list);
        }
        return false;
    }

    @Override
    public ByteTag remove(int index) {
        return NBTTag.wrap(list.remove(index));
    }

    @Override
    public ByteTag set(int index, ByteTag item) {
        return NBTTag.wrap(list.set(index, item.unwrap()));
    }

    @Override
    public ByteTag get(int index) {
        return NBTTag.wrap(list.get(index));
    }

    @Override
    public NBTTagByteArray unwrap() {
        return (NBTTagByteArray) super.unwrap();
    }

    public byte[] getBytes() {
        return unwrap().getBytes();
    }

    public void add(int index, byte b) {
        super.add(index, ByteTag.valueOf(b));
    }

    public void add(byte b) {
        super.add(ByteTag.valueOf(b));
    }

    public void set(int index, byte b) {
        set(index, ByteTag.valueOf(b));
    }
}
