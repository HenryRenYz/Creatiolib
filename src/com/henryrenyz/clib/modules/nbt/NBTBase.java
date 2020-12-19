package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;

public interface NBTBase {

    String toString();

    NBTType getType();

    NBTBase clone();

    Object getRaw();

    default byte getTypeId() {
        return getType().getId();
    }

    static NBTBase toBase(Object RawBase) {
        String name = RawBase.getClass().getCanonicalName();
        if (name.equalsIgnoreCase(ReflectionClass.NBTTagCompound.c.getCanonicalName())) {
            return new NBTCompound(RawBase);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagByte.c.getCanonicalName())) {
            return new NBTNumber(RawBase, NBTNumber.NumberType.BYTE);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagShort.c.getCanonicalName())) {
            return new NBTNumber(RawBase, NBTNumber.NumberType.SHORT);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagInt.c.getCanonicalName())) {
            return new NBTNumber(RawBase, NBTNumber.NumberType.INT);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagDouble.c.getCanonicalName())) {
            return new NBTNumber(RawBase, NBTNumber.NumberType.DOUBLE);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagLong.c.getCanonicalName())) {
            return new NBTNumber(RawBase, NBTNumber.NumberType.LONG);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagFloat.c.getCanonicalName())) {
            return new NBTNumber(RawBase, NBTNumber.NumberType.FLOAT);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagString.c.getCanonicalName())) {
            return new NBTString(RawBase);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagList.c.getCanonicalName())) {
            return new NBTList(RawBase);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagByteArray.c.getCanonicalName())) {
            return new NBTByteArray(RawBase);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagIntArray.c.getCanonicalName())) {
            return new NBTIntArray(RawBase);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagLongArray.c.getCanonicalName())) {
            return new NBTLongArray(RawBase);
        } else if (name.equalsIgnoreCase(ReflectionClass.NBTTagEnd.c.getCanonicalName())) {
            return new NBTEnd();
        }
        return null;
    }
}
