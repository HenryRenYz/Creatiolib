package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagEnd;

public class EndTag implements NBTTag<NBTTagEnd> {

    public static final EndTag INSTANCE = new EndTag();

    private final NBTTagEnd end;

    private EndTag() {
        end = NBTTagEnd.b;
    }

    @Override
    public NBTType getType() {
        return NBTType.END;
    }

    @Override
    public NBTTagEnd unwrap() {
        return end;
    }

    @Override
    public Class<? extends NBTTagEnd> wrappedClass() {
        return NBTTagEnd.class;
    }

    @Override
    public String toString() {
        return end.toString();
    }
}
