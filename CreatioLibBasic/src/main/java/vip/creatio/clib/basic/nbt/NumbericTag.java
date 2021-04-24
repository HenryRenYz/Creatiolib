package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTNumber;

public abstract class NumbericTag<T extends NBTNumber> implements NBTTag<T> {

    protected final T number;

    public NumbericTag(T number) {
        this.number = number;
    }

    public byte asByte() {
        return number.asByte();
    }

    public short asShort() {
        return number.asShort();
    }

    public int asInt() {
        return number.asInt();
    }

    public long asLong() {
        return number.asLong();
    }

    public float asFloat() {
        return number.asFloat();
    }

    public double asDouble() {
        return number.asDouble();
    }

    public Number asNumber() {
        return number.k();
    }

    @Override
    public T unwrap() {
        return number;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends T> wrappedClass() {
        return (Class) number.getClass();
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public Object clone() {
        return this;
    }

    @Override
    public int hashCode() {
        return number.hashCode() * 31;
    }
}
