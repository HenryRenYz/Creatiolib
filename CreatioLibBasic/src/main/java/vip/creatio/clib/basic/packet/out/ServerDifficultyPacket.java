package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.EnumDifficulty;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.util.BukkitUtil;
import net.minecraft.server.PacketPlayOutServerDifficulty;
import org.bukkit.Difficulty;

public class ServerDifficultyPacket extends Packet<PacketPlayOutServerDifficulty> {

    private static final Var<EnumDifficulty> DIFFICULTY = Reflection.field(PacketPlayOutServerDifficulty.class, "a");
    private static final Var<Boolean> LOCKED = Reflection.field(PacketPlayOutServerDifficulty.class, "b");

    private final Difficulty difficulty;
    private final boolean locked;

    ServerDifficultyPacket(PacketPlayOutServerDifficulty nms) {
        super(nms);
        this.difficulty = BukkitUtil.toBukkit(DIFFICULTY.get(nms));
        this.locked = LOCKED.getBoolean(nms);
    }

    /**
     * Packet for changing client server difficulty display.
     * The server difficulty will not be affected.
     *
     * @param diff difficulty which client should display
     *
     * @param locked is difficulty locked in options(seems to be useless).
     */
    public ServerDifficultyPacket(Difficulty diff, boolean locked) {
        super(new PacketPlayOutServerDifficulty(BukkitUtil.toNms(diff), locked));
        this.difficulty = diff;
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "ServerDifficulty{difficulty=" + difficulty.name().toLowerCase() + ",locked=" + locked + '}';
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean isLocked() {
        return locked;
    }
}
