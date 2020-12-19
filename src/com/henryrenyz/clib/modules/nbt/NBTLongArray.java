package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;

import java.util.List;

public class NBTLongArray extends NBTArray{

    private Object data;
    private static final Class clazz = ReflectionClass.NBTTagLongArray.c;

    public NBTLongArray(long[] array) {
        data = ReflectionConstructor.run(clazz, array);
    }
    public NBTLongArray(List<Long> array) {
        data = ReflectionConstructor.run(clazz, array);
    }

    NBTLongArray(Object nbtByteArray) {
        data = nbtByteArray;
    }

    @Override
    public NBTNumber set(int Index, NBTBase item) {
        return new NBTNumber((Long) ReflectionMethod.run(clazz, "set", data, Index, item));
    }

    @Override
    public void add(int Index, NBTBase item) {
        ReflectionMethod.run(clazz, "add", data, Index, item);
    }

    @Override
    public NBTNumber remove(int Index) {
        return new NBTNumber((Long) ReflectionMethod.run(clazz, "remove", data, Index));
    }

    @Override
    public int size() {
        return (Integer) ReflectionMethod.run(clazz, "size", data);
    }

    @Override
    public NBTNumber get(int Index) {
        return new NBTNumber(ReflectionMethod.run(clazz, "get", data, Index), NBTNumber.NumberType.LONG);
    }

    @Override
    public NBTType getType() {
        return NBTType.LONGARRAY;
    }

    public boolean equals(Object obj) {
        return (Boolean) ReflectionMethod.run(clazz, "equals", data, obj);
    }

    public void clear() {
        ReflectionMethod.run(clazz, "clear", data);
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
        return (String) ReflectionMethod.run(clazz, "toString", data);
    }
}
