package vip.creatio.clib.basic.nbt;

import net.minecraft.server.NBTTagByte;

public class ByteTag extends NumbericTag<NBTTagByte> {

    public ByteTag(NBTTagByte number) {
        super(number);
    }

    public static ByteTag valueOf(byte value) {
        return new ByteTag(NBTTagByte.a(value));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ByteTag) {
            return number.equals(((ByteTag) o).number);
        }
        return false;
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE;
    }
}
