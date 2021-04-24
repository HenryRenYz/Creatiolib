package vip.creatio.clib.basic.nbt;

import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.*;

public interface NBTTag<T extends NBTBase> extends Wrapper<T> {

    NBTType getType();

    default IChatBaseComponent getPrettyDisplay(String prefix, int prefixRepeat) {
        return unwrap().a(prefix, prefixRepeat);
    }

    default byte getTypeID() {
        return getType().getId();
    }

    T unwrap();

    @SuppressWarnings("unchecked")
    static <T extends NBTBase, S extends NBTTag<? extends T>> S wrap(T rawBase) {
        if (rawBase == null) return null;
        if (rawBase instanceof NBTTagCompound) {
            return (S) new CompoundTag((NBTTagCompound) rawBase);
        } else if (rawBase instanceof NBTNumber) {
            if (rawBase instanceof NBTTagByte) {
                return (S) new ByteTag((NBTTagByte) rawBase);
            } else if (rawBase instanceof NBTTagShort) {
                return (S) new ShortTag((NBTTagShort) rawBase);
            } else if (rawBase instanceof NBTTagInt) {
                return (S) new IntTag((NBTTagInt) rawBase);
            } else if (rawBase instanceof NBTTagDouble) {
                return (S) new DoubleTag((NBTTagDouble) rawBase);
            } else if (rawBase instanceof NBTTagFloat) {
                return (S) new FloatTag((NBTTagFloat) rawBase);
            } else if (rawBase instanceof NBTTagLong) {
                return (S) new LongTag((NBTTagLong) rawBase);
            }
        } else if (rawBase instanceof NBTList) {
            if (rawBase instanceof NBTTagList) {
                return (S) new ListTag<>((NBTTagList) rawBase);
            } else if (rawBase instanceof NBTTagIntArray) {
                return (S) new IntArrayTag((NBTTagIntArray) rawBase);
            } else if (rawBase instanceof NBTTagLongArray) {
                return (S) new LongArrayTag((NBTTagLongArray) rawBase);
            } else if (rawBase instanceof NBTTagByteArray) {
                return (S) new ByteArrayTag((NBTTagByteArray) rawBase);
            }
        } else if (rawBase instanceof NBTTagString) {
            return (S) new StringTag((NBTTagString) rawBase);
        } else if (rawBase instanceof NBTTagEnd) {
            return (S) EndTag.INSTANCE;
        }
        throw new RuntimeException("Unsupport instance of class " + rawBase.getClass().getTypeName());
    }
}
