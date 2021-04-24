package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.PacketPlayOutEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import vip.creatio.clib.basic.internal.Wrapped;

//TODO: waiting to be tested
@Wrapped(PacketPlayOutEntity.PacketPlayOutRelEntityMove.class)
public class EntityMovePacket extends EntityPositionPacket {

    EntityMovePacket(PacketPlayOutEntity.PacketPlayOutRelEntityMove nms) {
        super(nms, EID.getInt(nms));
        setValue();
    }

    public EntityMovePacket(int eid, double dx, double dy, double dz, boolean onGround) {
        super(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                eid,
                (short) (dx * 32 * 128),
                (short) (dy * 32 * 128),
                (short) (dz * 32 * 128),
                onGround), eid);
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.onGround = onGround;
    }

    public EntityMovePacket(int eid, Vector delta, boolean onGround) {
        this(eid, delta.getX(), delta.getY(), delta.getZ(), onGround);
    }

    public EntityMovePacket(Entity entity, double dx, double dy, double dz) {
        this(entity.getEntityId(), dx, dy, dz, entity.isOnGround());
    }

    public EntityMovePacket(Entity entity, Vector delta) {
        this(entity.getEntityId(), delta, entity.isOnGround());
    }

}
