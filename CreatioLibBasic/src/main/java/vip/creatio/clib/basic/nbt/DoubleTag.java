package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagDouble;

public class DoubleTag extends NumbericTag<NBTTagDouble> {

    public DoubleTag(NBTTagDouble number) {
        super(number);
    }

    public static DoubleTag valueOf(double value) {
        return new DoubleTag(NBTTagDouble.a(value));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DoubleTag) {
            return number.equals(((DoubleTag) o).number);
        }
        return false;
    }

    @Override
    public NBTType getType() {
        return NBTType.DOUBLE;
    }
}
