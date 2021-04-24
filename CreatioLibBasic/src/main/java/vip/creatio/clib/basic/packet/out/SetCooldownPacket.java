package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.Item;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.BukkitUtil;
import vip.creatio.clib.basic.util.ItemUtil;
import net.minecraft.server.PacketPlayOutSetCooldown;
import org.bukkit.Material;

public class SetCooldownPacket extends Packet<PacketPlayOutSetCooldown> {

    private static final Var<Item> MATERIAL = Reflection.field(PacketPlayOutSetCooldown.class, "a");
    private static final Var<Integer> TICK = Reflection.field(PacketPlayOutSetCooldown.class, "b");

    private final Material material;
    private final int tick;

    SetCooldownPacket(PacketPlayOutSetCooldown nms) {
        super(nms);
        this.material = ItemUtil.toBukkit(MATERIAL.get(nms));
        this.tick = TICK.getInt(nms);
    }

    /**
     * Packet for setting item cooldown for client, this will prevent
     * player  from doing anything using  specific item.  This is not
     * weapon cooldown, it's something like ender pearl cd.
     *
     * @param mat the item to be apply.
     * @param tick how long will the cooldown be.
     */
    public SetCooldownPacket(Material mat, int tick) {
        super(new PacketPlayOutSetCooldown(ItemUtil.toNms(mat), tick));
        this.material = mat;
        this.tick = tick;
    }

    @Override
    public String toString() {
        return "SetCooldown{material=" + material.getKey().toString() + ",tick=" + tick + '}';
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getTick() {
        return this.tick;
    }
}
