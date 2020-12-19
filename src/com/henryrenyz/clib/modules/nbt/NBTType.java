package com.henryrenyz.clib.modules.nbt;

import org.jetbrains.annotations.Nullable;;

public enum NBTType {
    END((byte) 0),
    BYTE((byte) 1),
    SHORT((byte) 2),
    INT((byte) 3),
    LONG((byte) 4),
    FLOAT((byte) 5),
    DOUBLE((byte) 6),
    BYTEARRAY((byte) 7),
    STRING((byte) 8),
    LIST((byte) 9),
    COMPOUND((byte) 10),
    INTARRAY((byte) 11),
    LONGARRAY((byte) 12);

    private byte id;
    NBTType(byte code) {
        this.id = code;
    }

    public byte getId() {
        return this.id;
    }

    public static @Nullable NBTType getType(byte id) {
        for (NBTType t : NBTType.values()) {
            if (t.getId() == id) return t;
        }
        return null;
    }
}
