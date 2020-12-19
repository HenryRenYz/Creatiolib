package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;

import java.util.List;

public class NBTByteArray extends NBTArray{

    private Object data;
    private static final Class Array = ReflectionClass.NBTTagByteArray.c;

    public NBTByteArray(byte[] array) {
        data = ReflectionConstructor.run(Array, array);
    }
    public NBTByteArray(List<Byte> array) {
        data = ReflectionConstructor.run(Array, array);
    }

    NBTByteArray (Object nbtByteArray) {
        data = nbtByteArray;
    }

    @Override
    public NBTNumber set(int Index, NBTBase item) {
        return new NBTNumber((Byte) ReflectionMethod.run(Array, "set", data, Index, item));
    }

    @Override
    public void add(int Index, NBTBase item) {
        ReflectionMethod.run(Array, "add", data, Index, item);
    }

    @Override
    public NBTNumber remove(int Index) {
        return new NBTNumber((Byte) ReflectionMethod.run(Array, "remove", data, Index));
    }

    @Override
    public int size() {
        return (Integer) ReflectionMethod.run(Array, "size", data);
    }

    @Override
    public NBTNumber get(int Index) {
        return new NBTNumber(ReflectionMethod.run(Array, "get", data, Index), NBTNumber.NumberType.BYTE);
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTEARRAY;
    }

    public boolean equals(Object obj) {
        return (Boolean) ReflectionMethod.run(Array, "equals", data, obj);
    }

    public void clear() {
        ReflectionMethod.run(Array, "clear", data);
    }

    @Override
    public NBTBase clone() {
        return this;
    }

    @Override
    public Object getRaw() {
        return data;
    }

    @Override
    public String toString() {
        return (String) ReflectionMethod.run(Array, "toString", data);
    }
}
