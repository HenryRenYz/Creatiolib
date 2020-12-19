package com.henryrenyz.clib.modules.nbt;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;
import org.jetbrains.annotations.Nullable;;

public class NBTNumber implements NBTBase{
    private final Object data;
    private final Class type;

    public NBTNumber(byte num) {
        data = ReflectionConstructor.run(ReflectionClass.NBTTagByte.c, num);
        type = ReflectionClass.NBTTagByte.c;
    }
    public NBTNumber(short num) {
        data = ReflectionConstructor.run(ReflectionClass.NBTTagShort.c, num);
        type = ReflectionClass.NBTTagShort.c;
    }
    public NBTNumber(int num) {
        data = ReflectionConstructor.run(ReflectionClass.NBTTagInt.c, num);
        type = ReflectionClass.NBTTagInt.c;
    }
    public NBTNumber(long num) {
        data = ReflectionConstructor.run(ReflectionClass.NBTTagLong.c, num);
        type = ReflectionClass.NBTTagLong.c;
    }
    public NBTNumber(float num) {
        data = ReflectionConstructor.run(ReflectionClass.NBTTagFloat.c, num);
        type = ReflectionClass.NBTTagFloat.c;
    }
    public NBTNumber(double num) {
        data = ReflectionConstructor.run(ReflectionClass.NBTTagDouble.c, num);
        type = ReflectionClass.NBTTagDouble.c;
    }
    NBTNumber(Object nbtTagBase, NumberType t) {
        data = nbtTagBase;
        type = t.getClazz();
    }

    public byte asByte() {
        return (Byte) ReflectionMethod.run(ReflectionClass.NBTTagByte.c, "asByte", data);
    }
    public short asShort() {
        return (Short) ReflectionMethod.run(ReflectionClass.NBTTagShort.c, "asShort", data);
    }
    public int asInt() {
        return (Integer) ReflectionMethod.run(ReflectionClass.NBTTagInt.c, "asInt", data);
    }
    public long asLong() {
        return (Long) ReflectionMethod.run(ReflectionClass.NBTTagLong.c, "asLong", data);
    }
    public float asFloat() {
        return (Float) ReflectionMethod.run(ReflectionClass.NBTTagFloat.c, "asFloat", data);
    }
    public double asDouble() {
        return (Double) ReflectionMethod.run(ReflectionClass.NBTTagDouble.c, "asDouble", data);
    }
    public Number asNumber() {
        return (Number) ReflectionMethod.run(type, "k", data);
    }

    @Override
    public @Nullable NBTType getType() {
        switch (type.getSimpleName()) {
            case "NBTTagByte":
                return NBTType.BYTE;
            case "NBTTagShort":
                return NBTType.SHORT;
            case "NBTTagInt":
                return NBTType.INT;
            case "NBTTagLong":
                return NBTType.LONG;
            case "NBTTagFloat":
                return NBTType.FLOAT;
            case "NBTTagDouble":
                return NBTType.DOUBLE;
        }
        return null;
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
        return (String) ReflectionMethod.run(type, "toString", data);
    }

    public enum NumberType {
        INT(ReflectionClass.NBTTagInt.c),
        BYTE(ReflectionClass.NBTTagByte.c),
        FLOAT(ReflectionClass.NBTTagFloat.c),
        DOUBLE(ReflectionClass.NBTTagDouble.c),
        LONG(ReflectionClass.NBTTagLong.c),
        SHORT(ReflectionClass.NBTTagShort.c);

        private Class clazz;

        NumberType(Class clazz) {
            this.clazz = clazz;
        }

        public Class getClazz() {
            return this.clazz;
        }

    }
}
