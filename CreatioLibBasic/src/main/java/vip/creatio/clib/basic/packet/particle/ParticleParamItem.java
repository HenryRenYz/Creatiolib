package vip.creatio.clib.basic.packet.particle;

import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.util.BukkitUtil;
import vip.creatio.clib.basic.util.ItemUtil;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class ParticleParamItem extends ParticleParam {

    private static final Var<net.minecraft.server.ItemStack> ITEMSTACK = Reflection.field(net.minecraft.server.ParticleParamItem.class, 1);

    private final ItemStack item;

    ParticleParamItem(net.minecraft.server.ParticleParamItem nms) {
        super(nms, BukkitUtil.toBukkit(nms.getParticle()));
        this.item = ItemUtil.toBukkit(ITEMSTACK.get(nms));
    }

    @SuppressWarnings("unchecked")
    public ParticleParamItem(Particle particle, ItemStack item) {
        super(new net.minecraft.server.ParticleParamItem(
                (net.minecraft.server.Particle<net.minecraft.server.ParticleParamItem>) BukkitUtil.toNms(particle),
                ItemUtil.toNms(item)), particle);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "ParticleParamItem{particle=" + particleType.name() + ",item=" + item.toString() + '}';
    }
}
