package vip.creatio.clib.basic.packet.out;

import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.common.Mth;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.PacketPlayOutEntity;

public abstract class EntityPositionPacket extends Packet<PacketPlayOutEntity> {

    static final Var<Integer> EID = Reflection.field(PacketPlayOutEntity.class, "a");
    static final Var<Short> DX = Reflection.field(PacketPlayOutEntity.class, "b");
    static final Var<Short> DY = Reflection.field(PacketPlayOutEntity.class, "c");
    static final Var<Short> DZ = Reflection.field(PacketPlayOutEntity.class, "d");
    static final Var<Byte> YAW = Reflection.field(PacketPlayOutEntity.class, "e");
    static final Var<Byte> PITCH = Reflection.field(PacketPlayOutEntity.class, "f");
    static final Var<Boolean> ON_GROUND = Reflection.field(PacketPlayOutEntity.class, "g");

    final int eid;
    double dx;
    double dy;
    double dz;
    float yaw;
    float pitch;
    boolean onGround;

    EntityPositionPacket(PacketPlayOutEntity pk, int eid) {
        super(pk);
        this.eid = eid;
    }

    void setValue() {
        this.dx = DX.getDouble(original);
        this.dy = DY.getDouble(original);
        this.dz = DZ.getDouble(original);
        this.yaw = Mth.byteToAngle(YAW.getByte(original));
        this.pitch = Mth.byteToAngle(PITCH.getByte(original));
    }

    public int getEntityID() {
        return eid;
    }

    public double getDX() {
        return dx;
    }

    public void setDX(double dx) {
        this.dx = dx;
        DX.set(unwrap(), (short) (dx * 32 * 128));
    }

    public double getDY() {
        return dy;
    }

    public void setDY(double dy) {
        this.dy = dy;
        DY.set(unwrap(), (short)(dy * 32 * 128));
    }

    public double getDZ() {
        return dz;
    }

    public void setDZ(double dz) {
        this.dz = dz;
        DZ.set(unwrap(), (short) (dz * 32 * 128));
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        YAW.set(unwrap(), Mth.angleToByte(yaw));
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        PITCH.set(unwrap(), Mth.angleToByte(pitch));
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        ON_GROUND.set(unwrap(), onGround);
        this.onGround = onGround;
    }
}
