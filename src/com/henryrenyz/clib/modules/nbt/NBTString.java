package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;

public class NBTString implements NBTBase {

    private final Object text;

    public NBTString(String text) {
        this.text = ReflectionConstructor.run(ReflectionClass.NBTTagString.c, text);
    }
    NBTString(Object nbtBase) {
        this.text = nbtBase;
    }

    @Override
    public NBTType getType() {
        return NBTType.STRING;
    }

    @Override
    public NBTBase clone() {
        return this;
    }

    @Override
    public Object getRaw() {
        return null;
    }

    @Override
    public String toString() {
        return (String) ReflectionMethod.run(ReflectionClass.NBTTagString.c, "toString", text);
    }
}
