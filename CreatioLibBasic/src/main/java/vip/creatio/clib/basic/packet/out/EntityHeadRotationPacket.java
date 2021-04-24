package vip.creatio.clib.basic.packet.out;

import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.EntityUtil;
import vip.creatio.common.Mth;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.PacketPlayOutEntityHeadRotation;
import org.bukkit.entity.Entity;

//TODO: waiting to be tested
public class EntityHeadRotationPacket extends Packet<PacketPlayOutEntityHeadRotation> {

    private static final Var<Integer> EID = Reflection.field(PacketPlayOutEntityHeadRotation.class, "a");
    private static final Var<Byte> RAW_YAW = Reflection.field(PacketPlayOutEntityHeadRotation.class, "b");

    private final int eid;
    private float yaw;

    EntityHeadRotationPacket(PacketPlayOutEntityHeadRotation nms) {
        super(nms);
        this.eid = EID.getInt(nms);
        this.yaw = Mth.byteToAngle(RAW_YAW.getByte(nms));
    }

    /** body yaw and head pitch */
    public EntityHeadRotationPacket(Entity entity, float yaw) {
        super(new PacketPlayOutEntityHeadRotation(EntityUtil.toNms(entity), Mth.angleToByte(yaw)));
        this.yaw = yaw;
        this.eid = entity.getEntityId();
    }

    public EntityHeadRotationPacket(int eid, float yaw) {
        super(new PacketPlayOutEntityHeadRotation());
        EID.setInt(original, eid);
        RAW_YAW.setInt(original, Mth.angleToByte(yaw));
        this.yaw = yaw;
        this.eid = eid;
    }

    public float getYaw() {
        return yaw;
    }

    public int getEntityID() {
        return eid;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        RAW_YAW.set(original, Mth.angleToByte(yaw));
    }
}
