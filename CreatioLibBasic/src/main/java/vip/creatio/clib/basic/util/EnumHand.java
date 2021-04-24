package vip.creatio.clib.basic.util;

import vip.creatio.clib.basic.tools.Wrapper;

public enum EnumHand implements Wrapper<net.minecraft.server.EnumHand> {
    MAIN_HAND(net.minecraft.server.EnumHand.MAIN_HAND, (byte) 0),
    OFF_HAND(net.minecraft.server.EnumHand.OFF_HAND, (byte) 1);

    private final net.minecraft.server.EnumHand original;
    private final byte id;

    EnumHand(net.minecraft.server.EnumHand nms, byte id) {
        this.original = nms;
        this.id = id;
    }

    public static EnumHand convert(net.minecraft.server.EnumHand nms) {
        for (EnumHand e : values()) {
            if (nms == e.original) return e;
        }
        return null;
    }

    @Override
    public net.minecraft.server.EnumHand unwrap() {
        return original;
    }

    @Override
    public Class<? extends net.minecraft.server.EnumHand> wrappedClass() {
        return net.minecraft.server.EnumHand.class;
    }


    public byte getId() {
        return id;
    }
}
