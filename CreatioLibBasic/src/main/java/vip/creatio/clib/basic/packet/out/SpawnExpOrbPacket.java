package vip.creatio.clib.basic.packet.out;

import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.BukkitUtil;
import vip.creatio.clib.basic.util.EntityUtil;
import vip.creatio.common.SysUtil;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.PacketPlayOutSpawnEntityExperienceOrb;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;

public class SpawnExpOrbPacket extends Packet<PacketPlayOutSpawnEntityExperienceOrb> {

    private static final Var<Integer> EID = Reflection.field(PacketPlayOutSpawnEntityExperienceOrb.class, 0);
    private static final Var<Double> X = Reflection.field(PacketPlayOutSpawnEntityExperienceOrb.class, 1);
    private static final Var<Double> Y = Reflection.field(PacketPlayOutSpawnEntityExperienceOrb.class, 2);
    private static final Var<Double> Z = Reflection.field(PacketPlayOutSpawnEntityExperienceOrb.class, 3);
    private static final Var<Integer> COUNT = Reflection.field(PacketPlayOutSpawnEntityExperienceOrb.class, 4);

    private final int eid;
    private final double x;
    private final double y;
    private final double z;
    private final int count;

    SpawnExpOrbPacket(PacketPlayOutSpawnEntityExperienceOrb nms) {
        super(nms);
        this.eid = EID.getInt(nms);
        this.x = X.getDouble(nms);
        this.y = Y.getDouble(nms);
        this.z = Z.getDouble(nms);
        this.count = COUNT.getInt(nms);
    }

    public SpawnExpOrbPacket(int eid, double x, double y, double z, int count) {
        super(new PacketPlayOutSpawnEntityExperienceOrb(SysUtil.exec(() -> {
            EntityExperienceOrb exp = new EntityExperienceOrb(BukkitUtil.DEFAULT_WORLD, 0, -10, 0, count);
            exp.setLocation(x, y, z, 0f, 0f);
            EntityUtil.setEntityId(exp, eid);
            return exp;
        })));
        this.eid = eid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }

    public SpawnExpOrbPacket(int eid, Location loc, int count) {
        this(eid, loc.getX(), loc.getY(), loc.getZ(), count);
    }

    public SpawnExpOrbPacket(ExperienceOrb entity, int count) {
        super(new PacketPlayOutSpawnEntityExperienceOrb((EntityExperienceOrb) EntityUtil.toNms(entity)));
        this.eid = entity.getEntityId();
        this.x = entity.getLocation().getX();
        this.y = entity.getLocation().getY();
        this.z = entity.getLocation().getZ();
        this.count = count;
    }

    public int getEntityId() {
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

    public int getCount() {
        return count;
    }
}
