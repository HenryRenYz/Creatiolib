package vip.creatio.clib.basic.util;

public enum EnumEntityAnimation {
    SWING_MAIN((byte) 0),
    TAKE_DAMAGE((byte) 1),
    LEAVE_BED((byte) 2),
    SWING_OFFHAND((byte) 3),
    CRIT_EFFECT((byte) 4),
    MAGIC_CRIT_EFFECT((byte) 5);

    private final byte id;

    EnumEntityAnimation(byte id) {
        this.id = id;
    }

    public byte getID() {
        return id;
    }
}
