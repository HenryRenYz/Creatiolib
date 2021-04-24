package vip.creatio.clib.basic.packet.out;

import vip.creatio.clib.basic.internal.Wrapped;
import vip.creatio.common.Mth;
import net.minecraft.server.PacketPlayOutEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

//TODO: waiting to be tested
@Wrapped(PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class)
public class EntityMoveLookPacket extends EntityPositionPacket {

    EntityMoveLookPacket(PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook nms) {
        super(nms, EID.getInt(nms));
        setValue();
    }

    /** body yaw and head pitch */
    public EntityMoveLookPacket(int eid, double dx, double dy, double dz, float yaw, float pitch, boolean onGround) {
        super(new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                eid,
                (short) (dx * 32 * 128),
                (short) (dy * 32 * 128),
                (short) (dz * 32 * 128),
                Mth.angleToByte(yaw),
                Mth.angleToByte(pitch),
                onGround), eid);
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public EntityMoveLookPacket(int eid, Vector delta, float yaw, float pitch, boolean onGround) {
        this(eid, delta.getX(), delta.getY(), delta.getZ(), yaw, pitch, onGround);
    }

    public EntityMoveLookPacket(Entity entity, double dx, double dy, double dz, float yaw, float pitch) {
        this(entity.getEntityId(), dx, dy, dz, yaw, pitch, entity.isOnGround());
    }

    public EntityMoveLookPacket(Entity entity, Vector delta, float yaw, float pitch) {
        this(entity.getEntityId(), delta, yaw, pitch, entity.isOnGround());
    }

}
