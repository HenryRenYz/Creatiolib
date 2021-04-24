package vip.creatio.clib.basic.packet.out;

import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.packet.particle.ParticleParam;
import vip.creatio.clib.basic.packet.particle.ParticleType;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class WorldParticlesPacket extends Packet<PacketPlayOutWorldParticles> {

    private static final Var<Double> X = Reflection.field(PacketPlayOutWorldParticles.class, 0);
    private static final Var<Double> Y = Reflection.field(PacketPlayOutWorldParticles.class, 1);
    private static final Var<Double> Z = Reflection.field(PacketPlayOutWorldParticles.class, 2);
    private static final Var<Float> DX = Reflection.field(PacketPlayOutWorldParticles.class, 3);
    private static final Var<Float> DY = Reflection.field(PacketPlayOutWorldParticles.class, 4);
    private static final Var<Float> DZ = Reflection.field(PacketPlayOutWorldParticles.class, 5);
    private static final Var<Float> EXTRA = Reflection.field(PacketPlayOutWorldParticles.class, 6);
    private static final Var<Integer> COUNT = Reflection.field(PacketPlayOutWorldParticles.class, 7);
    private static final Var<Boolean> FORCE = Reflection.field(PacketPlayOutWorldParticles.class, 8);
    private static final Var<net.minecraft.server.ParticleParam> PARAM = Reflection.field(PacketPlayOutWorldParticles.class, 9);

    private double x;
    private double y;
    private double z;
    private float dx;
    private float dy;
    private float dz;
    private float extra;
    private int count;
    private boolean force;
    private final ParticleParam param;

    WorldParticlesPacket(PacketPlayOutWorldParticles nms) {
        super(nms);
        this.x = X.getDouble(nms);
        this.y = Y.getDouble(nms);
        this.z = Z.getDouble(nms);
        this.dx = DX.getFloat(nms);
        this.dy = DY.getFloat(nms);
        this.dz = DZ.getFloat(nms);
        this.extra = EXTRA.getFloat(nms);
        this.count = COUNT.getInt(nms);
        this.force = FORCE.getBoolean(nms);
        this.param = ParticleParam.wrap(PARAM.get(nms));
    }

    public WorldParticlesPacket(ParticleParam param,
                                boolean force,
                                double x,
                                double y,
                                double z,
                                float dx,
                                float dy,
                                float dz,
                                float extra,
                                int count) {
        super(new PacketPlayOutWorldParticles(param.unwrap(), force, x, y, z, dx, dy, dz, extra, count));
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.extra = extra;
        this.count = count;
        this.force = force;
        this.param = param;
    }

    public WorldParticlesPacket(ParticleParam particle,
                                Location loc,
                                float dx,
                                float dy,
                                float dz,
                                float extra,
                                int count) {
        this(particle, true, loc.getX(), loc.getY(), loc.getZ(), dx, dy, dz, extra, count);
    }

    public WorldParticlesPacket(Particle particle,
                                Location loc,
                                float dx,
                                float dy,
                                float dz,
                                float extra,
                                int count) {
        this(new ParticleType(particle), true, loc.getX(), loc.getY(), loc.getZ(), dx, dy, dz, extra, count);
    }

    public WorldParticlesPacket(Particle particle,
                                Location loc,
                                int count) {
        this(new ParticleType(particle), true, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 0, count);
    }

    public WorldParticlesPacket(Particle particle,
                                Location loc) {
        this(new ParticleType(particle), true, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 0, 1);
    }

    public WorldParticlesPacket(ParticleParam param,
                                Location loc,
                                int count) {
        this(param, true, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 0, count);
    }

    public void setLocation(Location loc) {
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
    }

    public void setX(double x) {
        X.set(original, x);
        this.x = x;
    }

    public void setY(double y) {
        Y.set(original, y);
        this.y = y;
    }

    public void setZ(double z) {
        Z.set(original, z);
        this.z = z;
    }

    public void setVector(Vector vec) {
        setDX((float) vec.getX());
        setDY((float) vec.getY());
        setDZ((float) vec.getZ());
    }

    public void setDelta(float dx, float dy, float dz) {
        setDX(dx);
        setDY(dy);
        setDZ(dz);
    }

    public void setDX(float dx) {
        DX.set(original, dx);
        this.dx = dx;
    }

    public void setDY(float dy) {
        DY.set(original, dy);
        this.dy = dy;
    }

    public void setDZ(float dz) {
        DZ.set(original, dz);
        this.dz = dz;
    }

    public void setExtra(float extra) {
        EXTRA.set(original, extra);
        this.extra = extra;
    }

    public void setCount(int count) {
        COUNT.set(original, count);
        this.count = count;
    }

    public void setForce(boolean force) {
        FORCE.set(original, force);
        this.force = force;
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

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getDz() {
        return dz;
    }

    public float getExtra() {
        return extra;
    }

    public int getCount() {
        return count;
    }

    public boolean isForce() {
        return force;
    }

    public ParticleParam getParam() {
        return param;
    }

    @Override
    public String toString() {
        return "WorldParticles{param=" + param.toString() +
                ",location=[" + x + ',' + y + ',' + z +
                "],delta=[" + dx + ',' + dy + ',' + dz +
                "],extra=" + extra + ",count=" + count + ",force=" + force + '}';
    }
}
