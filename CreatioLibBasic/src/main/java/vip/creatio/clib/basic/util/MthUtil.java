package vip.creatio.clib.basic.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import vip.creatio.common.Mth;

import java.util.Random;

public final class MthUtil {

    private static final Random RANDOM = new Random();

    public static Vector randVecInRadius(double radius) {
        double deg = 2 * Math.PI * RANDOM.nextDouble();
        return new Vector(Math.sin(deg), 0, Math.cos(deg)).multiply(radius);
    }

    public static Vector[] randVecInHitbox(BoundingBox box, int randCount) {
        Vector[] vecs = new Vector[randCount];
        for (int i = 0; i < randCount; i++) {
            vecs[i] = new Vector(
                    Mth.nextDouble(box.getMinX() - box.getMaxX(), box.getMaxX() - box.getMinX()) / 2D,
                    Mth.nextDouble(box.getMinY() - box.getMaxY(), box.getMaxY() - box.getMinY()) / 2D,
                    Mth.nextDouble(box.getMinZ() - box.getMaxZ(), box.getMaxZ() - box.getMinZ()) / 2D);
        }
        return vecs;
    }

    public static Location[] randLocInHitbox(BoundingBox box, int randCount, World w) {
        Location[] locs = new Location[randCount];
        for (int i = 0; i < randCount; i++) {
            locs[i] = new Location(w,
                    Mth.nextDouble(box.getMinX(), box.getMaxX()),
                    Mth.nextDouble(box.getMinY(), box.getMaxY()),
                    Mth.nextDouble(box.getMinZ(), box.getMaxZ()));
        }
        return locs;
    }

    public static int deltaToInt(double delta) {
        return (int) (Mth.clamp(delta, -3.9D, 3.9D) * 8000D);
    }

    public static double intToDelta(int ser) {
        return ser / 8000D;
    }
}
