package vip.creatio.clib.basic.packet.out;

import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.BukkitUtil;
import vip.creatio.clib.basic.util.EntityUtil;
import vip.creatio.clib.basic.util.MthUtil;
import vip.creatio.common.Mth;
import vip.creatio.common.SysUtil;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.UUID;

/**
 * Spawn a living entity
 *
 * The data value acts differently with different EntityTypes
 * See https://wiki.vg/Object_Data
 */
public class SpawnLivingEntityPacket<T extends LivingEntity> extends Packet<PacketPlayOutSpawnEntityLiving> {

    private static final Var<Integer> EID = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 0);
    private static final Var<UUID> UUID = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 1);
    private static final Var<Integer> RAW_TYPE = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 2);
    private static final Var<Double> X = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 3);
    private static final Var<Double> Y = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 4);
    private static final Var<Double> Z = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 5);
    private static final Var<Integer> RAW_XA = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 6);
    private static final Var<Integer> RAW_YA = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 7);
    private static final Var<Integer> RAW_ZA = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 8);
    private static final Var<Byte> RAW_YAW = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 9);
    private static final Var<Byte> RAW_PITCH = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 10);
    private static final Var<Byte> RAW_HEAD_PITCH = Reflection.field(PacketPlayOutSpawnEntityLiving.class, 11);

    private final int eid;
    private final UUID uuid;
    private final EntityType type;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final float headPitch;
    private final double xa;
    private final double ya;
    private final double za;

    SpawnLivingEntityPacket(PacketPlayOutSpawnEntityLiving nms) {
        super(nms);
        this.eid = EID.getInt(nms);
        this.uuid = UUID.get(nms);
        this.type = EntityType.fromId(RAW_TYPE.getInt(nms));
        this.x = X.getDouble(nms);
        this.y = Y.getDouble(nms);
        this.z = Z.getDouble(nms);
        this.yaw = Mth.byteToAngle(RAW_YAW.getByte(nms));
        this.pitch = Mth.byteToAngle(RAW_PITCH.getByte(nms));
        this.headPitch = Mth.byteToAngle(RAW_HEAD_PITCH.getByte(nms));
        this.xa = MthUtil.intToDelta(RAW_XA.getInt(nms));
        this.ya = MthUtil.intToDelta(RAW_YA.getInt(nms));
        this.za = MthUtil.intToDelta(RAW_ZA.getInt(nms));
    }

    public SpawnLivingEntityPacket(int eid,
                                   UUID uuid,
                                   double x,
                                   double y,
                                   double z,
                                   float yaw,
                                   float pitch,
                                   float head_pitch,
                                   EntityType type,
                                   Vector motion) {
        this(SysUtil.exec(() -> {
            EntityLiving nms = (EntityLiving) EntityUtil.create(BukkitUtil.DEFAULT_WORLD, BukkitUtil.toNmsEntityType(type));
            assert nms != null;
            EntityUtil.setEntityId(nms, eid);
            EntityUtil.setUniqueId(nms, uuid);
            return nms;
        }), x, y, z, yaw, pitch, head_pitch, motion);
    }

    public SpawnLivingEntityPacket(T entity,
                                   double x,
                                   double y,
                                   double z,
                                   float yaw,
                                   float pitch,
                                   float head_pitch,
                                   Vector motion) {
        this(EntityUtil.toNms(entity), x, y, z, yaw, pitch, head_pitch, motion);
    }

    public SpawnLivingEntityPacket(EntityLiving nms,
                                   double x,
                                   double y,
                                   double z,
                                   float yaw,
                                   float pitch,
                                   float head_pitch,
                                   Vector motion) {
        super(new PacketPlayOutSpawnEntityLiving(SysUtil.exec(() -> {
            nms.setLocation(x, y, z, yaw, pitch);
            nms.setHeadRotation(head_pitch);
            nms.setMot(motion.getX(), motion.getY(), motion.getZ());
            return nms;
        })));
        this.eid = nms.getId();
        this.uuid = nms.getUniqueID();
        this.type = BukkitUtil.toBukkitEntityType(nms.getEntityType());
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = head_pitch;
        this.xa = motion.getX();
        this.ya = motion.getY();
        this.za = motion.getZ();
    }

    public SpawnLivingEntityPacket(T entity, Location loc, Vector motion) {
        this(entity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0f, loc.getPitch(), motion);
    }

    public SpawnLivingEntityPacket(T entity, Location loc) {
        this(entity, loc, new Vector(0, 0, 0));
    }

    public SpawnLivingEntityPacket(T entity) {
        this(entity.getEntityId(), entity.getUniqueId(), entity.getLocation().getX(), entity.getLocation().getY(),
                entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch(),
                0f, entity.getType(), entity.getVelocity());
    }

    public int getEntityId() {
        return eid;
    }

    public UUID getUniqueId() {
        return uuid;
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

    public Vector getMotion() {
        return new Vector(xa, ya, za);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public String toString() {
        Vector v = getMotion();
        return "SpawnEntity{type=" + type.getKey() + ",eid=" + getEntityId() + ",uuid=" + getUniqueId()
                + "location=[" + getX() + ',' + getY() + ',' + getZ() + ',' + getYaw() + "f," + getPitch() + "f],motion=["
                + v.getX() + ',' + v.getY() + ',' + v.getZ() + "],head_pitch=" + getHeadPitch() + '}';
    }
}
