package vip.creatio.clib.basic.util;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.util.BlockVector;

public final class BlockUtil {

    private BlockUtil() {}

    public static IBlockData toNms(BlockData data) {
        return ((CraftBlockData) data).getState();
    }

    public static BlockData toBukkit(IBlockData nms) {
        return CraftBlockData.fromData(nms);
    }

    public static BlockPosition toNmsPos(Block block) {
        return ((CraftBlock) block).getPosition();
    }

    public static BlockPosition toNms(BlockVector vec) {
        return new BlockPosition(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
    }

    public static IBlockData toNms(Block block) {
        return ((CraftBlock) block).getNMS();
    }

    public static Material getMat(net.minecraft.server.Block nms) {
        return CraftMagicNumbers.getMaterial(nms);
    }

    public static BlockFace toBukkit(EnumDirection dir) {
        return CraftBlock.notchToBlockFace(dir);
    }

    public static EnumDirection toNms(BlockFace face) {
        return CraftBlock.blockFaceToNotch(face);
    }

}
