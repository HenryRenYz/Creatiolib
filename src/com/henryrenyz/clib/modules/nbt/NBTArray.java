package com.henryrenyz.clib.modules.nbt;

import java.util.AbstractList;

public abstract class NBTArray<T extends NBTBase> extends AbstractList<T> implements NBTBase{

    public abstract T set(int Index, T item);

    public abstract void add(int Index, T item);

    public abstract T get(int Index);

    public abstract T remove(int Index);

    public abstract int size();

    public abstract T clone();

}
