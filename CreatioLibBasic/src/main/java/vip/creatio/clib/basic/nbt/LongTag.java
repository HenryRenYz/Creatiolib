package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagLong;

public class LongTag extends NumbericTag<NBTTagLong> {

    public LongTag(NBTTagLong number) {
        super(number);
    }

    public static LongTag valueOf(long value) {
        return new LongTag(NBTTagLong.a(value));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LongTag) {
            return number.equals(((LongTag) o).number);
        }
        return false;
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG;
    }
}
