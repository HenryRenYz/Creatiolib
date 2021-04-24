package vip.creatio.clib.basic.packet.out;

import org.bukkit.Location;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.EntityUtil;
import net.minecraft.server.PacketPlayOutEntityTeleport;
import org.bukkit.entity.Entity;
import vip.creatio.common.Mth;

public class EntityTeleportPacket extends Packet<PacketPlayOutEntityTeleport> {

    private static final Var<Integer> EID = Reflection.field(PacketPlayOutEntityTeleport.class, "a");
    private static final Var<Double> X = Reflection.field(PacketPlayOutEntityTeleport.class, "b");
    private static final Var<Double> Y = Reflection.field(PacketPlayOutEntityTeleport.class, "c");
    private static final Var<Double> Z = Reflection.field(PacketPlayOutEntityTeleport.class, "d");
    private static final Var<Byte> YAW = Reflection.field(PacketPlayOutEntityTeleport.class, "e");
    private static final Var<Byte> PITCH = Reflection.field(PacketPlayOutEntityTeleport.class, "f");
    private static final Var<Boolean> ON_GROUND = Reflection.field(PacketPlayOutEntityTeleport.class, "g");

    private final int eid;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    EntityTeleportPacket(PacketPlayOutEntityTeleport nms) {
        super(nms);
        this.eid = EID.getInt(nms);
        this.x = X.getDouble(nms);
        this.y = Y.getDouble(nms);
        this.z = Z.getDouble(nms);
        this.yaw = Mth.byteToAngle(YAW.getByte(nms));
        this.pitch = Mth.byteToAngle(PITCH.getByte(nms));
        this.onGround = ON_GROUND.getBoolean(nms);
    }

    public EntityTeleportPacket(Entity entity) {
        this(new PacketPlayOutEntityTeleport(EntityUtil.toNms(entity)));
    }

    public EntityTeleportPacket(int eid, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        super(new PacketPlayOutEntityTeleport());
        EID.setInt(original, eid);
        X.setDouble(original, x);
        Y.setDouble(original, y);
        Z.setDouble(original, z);
        YAW.setByte(original, Mth.angleToByte(yaw));
        PITCH.setByte(original, Mth.angleToByte(pitch));
        ON_GROUND.setBoolean(original, onGround);

        this.eid = eid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public EntityTeleportPacket(int eid, Location loc, boolean onGround) {
        this(eid, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }

    public EntityTeleportPacket(int eid, Location loc) {
        this(eid, loc, false);
    }

    public int getEntityID() {
        return eid;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setX(double x) {
        X.setDouble(x);
        this.x = x;
    }

    public void setY(double y) {
        Y.setDouble(y);
        this.y = y;
    }

    public void setZ(double z) {
        Z.setDouble(z);
        this.z = z;
    }

    public void setYaw(float yaw) {
        YAW.setByte(Mth.angleToByte(yaw));
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        PITCH.setByte(Mth.angleToByte(pitch));
        this.pitch = pitch;
    }

    public void setOnGround(boolean onGround) {
        ON_GROUND.setBoolean(onGround);
        this.onGround = onGround;
    }
}
