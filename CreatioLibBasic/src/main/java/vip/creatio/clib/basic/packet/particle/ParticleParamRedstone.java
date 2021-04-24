package vip.creatio.clib.basic.packet.particle;

import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import org.bukkit.Particle;
import vip.creatio.clib.basic.util.BukkitUtil;

public class ParticleParamRedstone extends ParticleParam {

    private static final Var<Float> RED = Reflection.field(net.minecraft.server.ParticleParamRedstone.class, 0);
    private static final Var<Float> GREEN = Reflection.field(net.minecraft.server.ParticleParamRedstone.class, 1);
    private static final Var<Float> BLUE = Reflection.field(net.minecraft.server.ParticleParamRedstone.class, 2);
    private static final Var<Float> SIZE = Reflection.field(net.minecraft.server.ParticleParamRedstone.class, 3);

    private float red;
    private float green;
    private float blue;
    private float size;

    ParticleParamRedstone(net.minecraft.server.ParticleParamRedstone nms) {
        super(nms, BukkitUtil.toBukkit(nms.getParticle()));
        this.red = RED.getFloat(nms);
        this.green = GREEN.getFloat(nms);
        this.blue = BLUE.getFloat(nms);
        this.size = SIZE.getFloat(nms);
    }

    public ParticleParamRedstone(float red, float green, float blue, float size) {
        super(new net.minecraft.server.ParticleParamRedstone(red, green, blue, size), Particle.REDSTONE);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.size = size;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getSize() {
        return size;
    }

    public void setColor(float r, float g, float b) {
        setRed(r);
        setGreen(g);
        setBlue(b);
    }

    public void setColor(float r, float g, float b, float size) {
        setRed(r);
        setGreen(g);
        setBlue(b);
        setSize(size);
    }

    public void setRed(float num) {
        RED.set(original, num);
        red = num;
    }

    public void setGreen(float num) {
        GREEN.set(original, num);
        green = num;
    }

    public void setBlue(float num) {
        BLUE.set(original, num);
        blue = num;
    }

    public void setSize(float num) {
        SIZE.set(original, num);
        size = num;
    }

    @Override
    public String toString() {
        return "ParticleParamRedstone{particle=" + Particle.REDSTONE.name() + ",red=" + red + ",green=" + green + ",blue=" + blue + ",size=" + size + '}';
    }
}
