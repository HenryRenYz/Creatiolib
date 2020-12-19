package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;
import org.jetbrains.annotations.Nullable;;

import java.util.ArrayList;
import java.util.List;

//wrapped class of NBTTagList
public class NBTList extends NBTArray<NBTBase> implements List<NBTBase> {

    private Object tagList = null;
    private static final Class clazz = ReflectionClass.NBTTagList.c;

    public NBTList(Object nbtTagList) {
        this.tagList = nbtTagList;
    }

    public NBTList(@Nullable NBTList clone) {
        this.tagList = ReflectionMethod.run(clazz, "clone", clone.getRaw());
    }

    public NBTList(List<NBTBase> list, NBTType type) {
        List<Object> ori = new ArrayList<>();
        for (NBTBase b : list) {
            ori.add(b.getRaw());
        }
        this.tagList = ReflectionConstructor.run(ReflectionClass.NBTTagList.c, ori, type.getId());
    }

    public NBTList(NBTType type) {
        this.tagList = ReflectionConstructor.run(ReflectionClass.NBTTagList.c, new ArrayList<>(), type.getId());
    }

    @Override
    public String toString() {
        if (tagList != null) {
            return (String) ReflectionMethod.run(clazz, "toString", tagList);
        }
        return "[]";
    }

    @Override
    public NBTType getType() {
        return NBTType.LIST;
    }

    @Override
    public Object getRaw() {
        return this.tagList;
    }

    @Override
    public NBTList clone() {
        return new NBTList(this);
    }

    @Override
    public NBTBase remove(int Index) {
        return NBTBase.toBase(ReflectionMethod.run(clazz, "remove", tagList, Index));
    }

    @Override
    public NBTBase set(int Index, NBTBase item) {
        return NBTBase.toBase(ReflectionMethod.run(ReflectionMethod.NBTTagList_set.m, tagList, Index, item.getRaw()));
    }

    @Override
    public void add(int Index, NBTBase item) {
        ReflectionMethod.run(ReflectionMethod.NBTTagList_add.m, tagList, Index, item.getRaw());
    }

    @Override
    public boolean add(NBTBase item) {
        ReflectionMethod.run(ReflectionMethod.NBTTagList_add.m, tagList, size(), item.getRaw());
        return true;
    }

    public boolean isEmpty() {
        return (Boolean) ReflectionMethod.run(clazz, "isEmpty", tagList);
    }

    @Override
    public int size() {
        return (Integer) ReflectionMethod.run(clazz, "size", tagList);
    }

    public boolean equals(Object obj) {
        return (Boolean) ReflectionMethod.run(clazz, "equals", tagList, obj);
    }


    //return the NBTCompound at the specific index, this will return a new compound if the Index is out of range
    public NBTCompound getCompound(int Index) {
        return new NBTCompound(ReflectionMethod.run(clazz, "getCompound", tagList, Index));
    }
    //return the NBTList at the specific index, same as the one above.
    public NBTList getList(int Index) {
        return new NBTList(ReflectionMethod.run(clazz, "b", tagList, Index));
    }
    //return the number of specific type at specific index, return 0 if the Index is out of range
    public short getShort(int Index) {                                              //Get Short
        return (Short) ReflectionMethod.run(clazz, "d", tagList, Index);
    }
    public int getInt(int Index) {                                                  //Get Integer
        return (Integer) ReflectionMethod.run(clazz, "e", tagList, Index);
    }
    public int[] getIntArray(int Index) {                                           //Get Int Array
        return (int[]) ReflectionMethod.run(clazz, "f", tagList, Index);
    }
    public double getDouble(int Index) {                                            //Get Double
        return (Double) ReflectionMethod.run(clazz, "h", tagList, Index);
    }
    public float getFloat(int Index) {                                              //Get Float
        return (Float) ReflectionMethod.run(clazz, "i", tagList, Index);
    }
    public String getString(int Index) {                                            //Get String
        return (String) ReflectionMethod.run(clazz, "getString", tagList, Index);
    }

    @Override
    public NBTBase get(int Index) {                                                 //General Get
        return NBTBase.toBase(ReflectionMethod.run(clazz, "get", tagList, Index));
    }

    public NBTType getListType() {
        return NBTType.getType((Byte) ReflectionMethod.run(ReflectionClass.NBTTagList.c, "d_", tagList));
    }

}
