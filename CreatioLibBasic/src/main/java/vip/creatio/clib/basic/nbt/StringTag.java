package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagString;

public class StringTag implements NBTTag<NBTTagString> {

    private final NBTTagString string;

    public StringTag(String text) {
        this.string = NBTTagString.a(text);
    }

    public StringTag(NBTTagString tagString) {
        this.string = tagString;
    }

    @Override
    public NBTType getType() {
        return NBTType.STRING;
    }

    @Override
    public NBTTagString unwrap() {
        return string;
    }

    @Override
    public Class<? extends NBTTagString> wrappedClass() {
        return NBTTagString.class;
    }

    @Override
    public Object clone() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringTag) {
            return string.equals(o);
        }
         return false;
    }

    @Override
    public int hashCode() {
        return string.hashCode() * 31;
    }

    public String asString() {
        return string.asString();
    }
}
