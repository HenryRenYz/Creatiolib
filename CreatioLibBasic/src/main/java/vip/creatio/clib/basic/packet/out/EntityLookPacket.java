package vip.creatio.clib.basic.packet.out;

import vip.creatio.clib.basic.internal.Wrapped;
import vip.creatio.common.Mth;
import net.minecraft.server.PacketPlayOutEntity;
import org.bukkit.entity.Entity;

//TODO: waiting to be tested
@Wrapped(PacketPlayOutEntity.PacketPlayOutEntityLook.class)
public class EntityLookPacket extends EntityPositionPacket {

    EntityLookPacket(PacketPlayOutEntity.PacketPlayOutEntityLook nms) {
        super(nms, EID.getInt(nms));
        setValue();
    }

    /** body yaw and head pitch */
    public EntityLookPacket(int eid, float yaw, float pitch, boolean onGround) {
        super(new PacketPlayOutEntity.PacketPlayOutEntityLook(eid, Mth.angleToByte(yaw), Mth.angleToByte(pitch), onGround), eid);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround; }

    public EntityLookPacket(Entity entity, float yaw, float pitch) {
        this(entity.getEntityId(), yaw, pitch, entity.isOnGround());
    }
}
