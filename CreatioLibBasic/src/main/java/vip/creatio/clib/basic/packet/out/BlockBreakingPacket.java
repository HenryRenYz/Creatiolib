package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.BlockPosition;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.packet.VirtualEntity;
import vip.creatio.clib.basic.util.BlockUtil;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.PacketPlayOutBlockBreakAnimation;
import org.bukkit.block.Block;
import vip.creatio.clib.basic.util.Vec3i;

import java.io.IOException;

public class BlockBreakingPacket extends Packet<PacketPlayOutBlockBreakAnimation> {

    private static final Var<Integer> EID = Reflection.field(PacketPlayOutBlockBreakAnimation.class, 0);
    private static final Var<BlockPosition> POS = Reflection.field(PacketPlayOutBlockBreakAnimation.class, 1);
    private static final Var<Integer> PROCESS = Reflection.field(PacketPlayOutBlockBreakAnimation.class, 2);

    private final int eid;
    private final Vec3i block;
    /** 0-9 process, other number cancel the process. */
    private int process;

    BlockBreakingPacket(PacketPlayOutBlockBreakAnimation nms) {
        super(nms);
        this.eid = EID.getInt(nms);
        this.block = new Vec3i(POS.get(nms));
        this.process = PROCESS.getInt(nms);
    }

    public BlockBreakingPacket(int eid, Block block, int process) {
        super(new PacketPlayOutBlockBreakAnimation(eid, BlockUtil.toNmsPos(block), process));
        this.eid = eid;
        this.block = new Vec3i(block.getX(), block.getY(), block.getZ());
        this.process = process;
    }

    public BlockBreakingPacket(VirtualEntity<?> entity, Block block, int process) {
        this(entity.getEntityId(), block, process);
    }

    public BlockBreakingPacket(int eid, Vec3i block, int process) {
        super(new PacketPlayOutBlockBreakAnimation(eid, new BlockPosition(block.unwrap()), process));
        this.eid = eid;
        this.block = block;
        this.process = process;
    }

    public BlockBreakingPacket(VirtualEntity<?> entity, Vec3i block, int process) {
        this(entity.getEntityId(), block, process);
    }

    public int getEntityID() {
        return eid;
    }

    public Vec3i getBlock() {
        return block;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        PROCESS.set(original, process);
        this.process = process;
    }
}
