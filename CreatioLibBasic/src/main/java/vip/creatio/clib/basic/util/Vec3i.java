package vip.creatio.clib.basic.util;

import net.minecraft.server.BaseBlockPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.common.Mth;
import vip.creatio.common.annotation.Immutable;

/**
 * Immutable block vector
 */
@Immutable
public class Vec3i implements Wrapper<BaseBlockPosition>, Comparable<Vec3i> {

    public static final Vec3i ZERO = new Vec3i(0, 0, 0);
    private static final int PACKED_X_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));;
    private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;
    private static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
    private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
    private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
    private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
    private static final int Z_OFFSET = PACKED_Y_LENGTH;
    private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;

    protected final BaseBlockPosition vec;

    public Vec3i(BaseBlockPosition nms) {
        this.vec = nms;
    }

    public Vec3i(int x, int y, int z) {
        this.vec = new BaseBlockPosition(x, y, z);
    }

    public Vec3i(double x, double y, double z) {
        this.vec = new BaseBlockPosition(x, y, z);
    }

    public Vec3i(Vector vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vec3i(Location loc) {
        this(loc.getX(), loc.getY(), loc.getZ());
    }

    public Vec3i(Vec3i vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vec3i(Block b) {
        this(b.getX(), b.getY(), b.getZ());
    }

    public int getX() {
        return vec.getX();
    }

    public int getY() {
        return vec.getY();
    }

    public int getZ() {
        return vec.getZ();
    }

    public Vec3i above() {
        return new Vec3i(vec.up());
    }

    public Vec3i above(int v) {
        return new Vec3i(vec.up(v));
    }

    public Vec3i below() {
        return new Vec3i(vec.down());
    }

    public Vec3i below(int v) {
        return new Vec3i(vec.down(v));
    }

    public Vec3i relative(BlockFace dir, int v) {
        return new Vec3i(vec.shift(BlockUtil.toNms(dir), v));
    }

    public Vec3i subtract(Vec3i v) {
        return new Vec3i(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
    }

    public Vec3i add(Vec3i v) {
        return new Vec3i(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
    }

    public Vec3i divide(Vec3i v) {
        return new Vec3i(getX() / v.getX(), getY() / v.getY(), getZ() / v.getZ());
    }

    public Vec3i multiply(Vec3i v) {
        return new Vec3i(getX() * v.getX(), getY() * v.getY(), getZ() * v.getZ());
    }

    public Vec3i cross(Vec3i v) {
        return new Vec3i(vec.d /* cross */ (v.vec));
    }

    public double dot(Vec3i v) {
        return getX() * v.getX() + getY() * v.getY() + getZ() + v.getZ();
    }

    public Vec3i multiply(double m) {
        return new Vec3i(getX() * m, getY() * m, getZ() * m);
    }

    public Vec3i midpoint(Vec3i v) {
        return new Vec3i((getX() + v.getX()) >> 2, (getY() + v.getY()) >> 2, (getZ() + v.getZ()) >> 2);
    }

    public double length() {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    public float angle(Vec3i v) {
        return (float) Math.acos(Mth.clamp(dot(v) / (length() * v.length()), -1.0, 1.0));
    }

    public double distSqr(Vec3i v) {
        return vec.j /* distSqr */ (v.vec);
    }

    /** With centerOffset true, the x, y, z of this vec will be increased by 0.5 in calculation */
    public double distSqr(Vector v, boolean centerOffset) {
        return distSqr(v.getX(), v.getY(), v.getZ(), centerOffset);
    }

    public double distSqr(Vector v) {
        return distSqr(v, false);
    }

    public double distSqr(Location loc, boolean centerOffset) {
        return distSqr(loc.getX(), loc.getY(), loc.getZ(), centerOffset);
    }

    public double distSqr(Location loc) {
        return distSqr(loc, false);
    }

    public double distSqr(double x, double y, double z, boolean centerOffset) {
        return vec.distanceSquared(x, y, z, centerOffset);
    }

    public double distSqr(double x, double y, double z) {
        return distSqr(x, y, z, false);
    }

    public double dist(double x, double y, double z, boolean centerOffset) {
        return Math.sqrt(distSqr(x, y, z, centerOffset));
    }

    public double dist(double x, double y, double z) {
        return dist(x, y, z, false);
    }

    public double dist(Vec3i v) {
        return Math.sqrt(distSqr(v));
    }

    public double dist(Vector vec) {
        return dist(vec.getX(), vec.getY(), vec.getZ());
    }

    public double dist(Location loc, boolean centerOffset) {
        return dist(loc.getX(), loc.getY(), loc.getZ(), centerOffset);
    }

    public double dist(Location loc) {
        return dist(loc, false);
    }

    public BlockVector toVec() {
        return new BlockVector(getX(), getY(), getZ());
    }

    public Location toLocation(World world) {
        return new Location(world, getX(), getY(), getZ());
    }

    public Block getBlock(World world) {
        return world.getBlockAt(getX(), getY(), getZ());
    }

    public int distManhattan(Vec3i v) {
        return vec.k /* distManhattan */ (v.vec);
    }

    public static Vec3i of(long l) {
        return new Vec3i(getX(l), getY(l), getZ(l));
    }

    public long asLong() {
        return asLong(getX(), getY(), getZ());
    }

    public static long asLong(int x, int y, int z) {
        long v = 0L;
        v |= ((long) x & PACKED_X_MASK) << X_OFFSET;
        v |= ((long) y & PACKED_Y_MASK);
        v |= ((long) z & PACKED_Z_MASK) << Z_OFFSET;
        return v;
    }

    public static int getX(long v) {
        return (int) (v << 64 - X_OFFSET - PACKED_X_LENGTH >> 64 - PACKED_X_LENGTH);
    }

    public static int getY(long v) {
        return (int) (v << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
    }

    public static int getZ(long v) {
        return (int) (v << 64 - Z_OFFSET - PACKED_Z_LENGTH >> 64 - PACKED_Z_LENGTH);
    }

    @Override
    public int hashCode() {
        return vec.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec3i) {
            return vec.equals(((Vec3i) obj).vec);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vec3i{x=" + getX() + ",y=" + getY() + ",z=" + getZ() + '}';
    }

    @Override
    public int compareTo(@NotNull Vec3i o) {
        return vec.compareTo(o.vec);
    }

    @Override
    public BaseBlockPosition unwrap() {
        return vec;
    }

    @Override
    public Class<? extends BaseBlockPosition> wrappedClass() {
        return vec.getClass();
    }
}
