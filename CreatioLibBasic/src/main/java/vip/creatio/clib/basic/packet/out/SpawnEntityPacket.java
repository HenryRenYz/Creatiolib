package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.EntityTypes;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.BukkitUtil;
import net.minecraft.server.PacketPlayOutSpawnEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import vip.creatio.clib.basic.util.MthUtil;
import vip.creatio.common.Mth;

import java.util.UUID;

/**
 * Spawn a non-living entity, etc minecart, arrow...
 *
 * The data value acts differently with different EntityTypes
 * See https://wiki.vg/Object_Data
 */
public class SpawnEntityPacket<T extends Entity> extends Packet<PacketPlayOutSpawnEntity> {

    private static final Var<Integer> EID = Reflection.field(PacketPlayOutSpawnEntity.class, 0);
    private static final Var<UUID> UUID = Reflection.field(PacketPlayOutSpawnEntity.class, 1);
    private static final Var<Double> X = Reflection.field(PacketPlayOutSpawnEntity.class, 2);
    private static final Var<Double> Y = Reflection.field(PacketPlayOutSpawnEntity.class, 3);
    private static final Var<Double> Z = Reflection.field(PacketPlayOutSpawnEntity.class, 4);
    private static final Var<Integer> RAW_XA = Reflection.field(PacketPlayOutSpawnEntity.class, 5);
    private static final Var<Integer> RAW_YA = Reflection.field(PacketPlayOutSpawnEntity.class, 6);
    private static final Var<Integer> RAW_ZA = Reflection.field(PacketPlayOutSpawnEntity.class, 7);
    private static final Var<Integer> RAW_YAW = Reflection.field(PacketPlayOutSpawnEntity.class, 8);
    private static final Var<Integer> RAW_PITCH = Reflection.field(PacketPlayOutSpawnEntity.class, 9);
    private static final Var<EntityTypes<?>> TYPE = Reflection.field(PacketPlayOutSpawnEntity.class, 10);
    private static final Var<Integer> DATA = Reflection.field(PacketPlayOutSpawnEntity.class, 11);

    private final int eid;
    private final UUID uuid;
    private final double x;
    private final double y;
    private final double z;
    private final double xa;
    private final double ya;
    private final double za;
    private final float yaw;
    private final float pitch;
    private final EntityType type;
    private final int data;

    SpawnEntityPacket(PacketPlayOutSpawnEntity nms) {
        super(nms);
        this.eid = EID.getInt(nms);
        this.uuid = UUID.get(nms);
        this.x = X.getDouble(nms);
        this.y = Y.getDouble(nms);
        this.z = Z.getDouble(nms);
        this.xa = MthUtil.intToDelta(RAW_XA.getInt(nms));
        this.ya = MthUtil.intToDelta(RAW_YA.getInt(nms));
        this.za = MthUtil.intToDelta(RAW_ZA.getInt(nms));
        this.yaw = Mth.byteToAngle(RAW_YAW.getByte(nms));
        this.pitch = Mth.byteToAngle(RAW_PITCH.getByte(nms));
        this.type = BukkitUtil.toBukkitEntityType(TYPE.get(nms));
        this.data = DATA.getInt(nms);
    }

    public SpawnEntityPacket(int eid,
                             UUID uuid,
                             double x,
                             double y,
                             double z,
                             float yaw,
                             float pitch,
                             EntityType type,
                             int data,
                             Vector motion) {
        super(new PacketPlayOutSpawnEntity(
                eid,
                uuid,
                x,
                y,
                z,
                yaw,
                pitch,
                BukkitUtil.toNmsEntityType(type),
                data,
                BukkitUtil.toNms(motion)));
        this.eid = eid;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xa = motion.getX();
        this.ya = motion.getY();
        this.za = motion.getZ();
        this.yaw = yaw;
        this.pitch = pitch;
        this.type = type;
        this.data = data;
    }

    public SpawnEntityPacket(Entity entity, double x, double y, double z, float yaw, float pitch, int data, Vector motion) {
        this(entity.getEntityId(), entity.getUniqueId(), x, y, z, yaw, pitch, entity.getType(), data, motion);
    }

    public SpawnEntityPacket(Entity entity, Location loc, int data, Vector motion) {
        this(entity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), data, motion);
    }

    public SpawnEntityPacket(Entity entity, Location loc) {
        this(entity, loc, 0, new Vector(0, 0, 0));
    }

    public SpawnEntityPacket(Entity entity, int data) {
        this(   entity.getEntityId(),
                entity.getUniqueId(),
                entity.getLocation().getX(),
                entity.getLocation().getY(),
                entity.getLocation().getZ(),
                entity.getLocation().getYaw(),
                entity.getLocation().getPitch(),
                entity.getType(),
                data,
                entity.getVelocity());
    }

    public SpawnEntityPacket(Entity entity) {
        this(entity, 0);
    }

    public int getEntityId() {
        return eid;
    }

    public UUID getUUID() {
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

    public double getXMotion() {
        return xa;
    }

    public double getYMotion() {
        return ya;
    }

    public double getZMotion() {
        return za;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getData() {
        return data;
    }


    @Override
    public String toString() {
        return "SpawnEntity{type=" + type.getKey() + ",eid=" + eid + ",uuid=" + uuid
                + ",location=[" + x + ',' + y + ',' + z + ',' + yaw + "f," + pitch + "f],motion=["
                + xa + ',' + ya + ',' + za + "],data=" + data + '}';
    }
}
