package vip.creatio.clib.basic.nbt;


import net.minecraft.server.NBTTagShort;

public class ShortTag extends NumbericTag<NBTTagShort> {

    public ShortTag(NBTTagShort number) {
        super(number);
    }

    public static ShortTag valueOf(short value) {
        return new ShortTag(NBTTagShort.a(value));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShortTag) {
            return number.equals(((ShortTag) o).number);
        }
        return false;
    }

    @Override
    public NBTType getType() {
        return NBTType.SHORT;
    }
}
