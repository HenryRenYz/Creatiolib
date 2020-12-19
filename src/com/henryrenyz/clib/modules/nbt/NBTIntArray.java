package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;

import java.util.List;

public class NBTIntArray extends NBTArray{

    private Object data;
    private static final Class clazz = ReflectionClass.NBTTagIntArray.c;

    public NBTIntArray(int[] array) {
        data = ReflectionConstructor.run(clazz, array);
    }
    public NBTIntArray(List<Integer> array) {
        data = ReflectionConstructor.run(clazz, array);
    }

    NBTIntArray(Object nbtByteArray) {
        data = nbtByteArray;
    }

    @Override
    public NBTNumber set(int Index, NBTBase item) {
        return new NBTNumber((Integer) ReflectionMethod.run(clazz, "set", data, Index, item));
    }

    @Override
    public void add(int Index, NBTBase item) {
        ReflectionMethod.run(clazz, "add", data, Index, item);
    }

    @Override
    public NBTNumber remove(int Index) {
        return new NBTNumber((Integer) ReflectionMethod.run(clazz, "remove", data, Index));
    }

    @Override
    public int size() {
        return (Integer) ReflectionMethod.run(clazz, "size", data);
    }

    @Override
    public NBTNumber get(int Index) {
        return new NBTNumber(ReflectionMethod.run(clazz, "get", data, Index), NBTNumber.NumberType.INT);
    }

    @Override
    public NBTType getType() {
        return NBTType.INTARRAY;
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
