package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagFloat;

public class FloatTag extends NumbericTag<NBTTagFloat> {

    public FloatTag(NBTTagFloat number) {
        super(number);
    }

    public static FloatTag valueOf(float value) {
        return new FloatTag(NBTTagFloat.a(value));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FloatTag) {
            return number.equals(((FloatTag) o).number);
        }
        return false;
    }

    @Override
    public NBTType getType() {
        return NBTType.FLOAT;
    }
}
