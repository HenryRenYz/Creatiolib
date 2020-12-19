package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;

public class NBTEnd implements NBTBase{

    private Object item;

    NBTEnd() {
        item = ReflectionConstructor.run(ReflectionClass.NBTTagEnd.c);
    }

    @Override
    public NBTType getType() {
        return NBTType.END;
    }

    @Override
    public NBTBase clone() {
        return this;
    }

    @Override
    public Object getRaw() {
        return item;
    }

    @Override
    public String toString() {
        return (String) ReflectionMethod.run(ReflectionClass.NBTTagEnd.c, "toString", item);
    }
}
