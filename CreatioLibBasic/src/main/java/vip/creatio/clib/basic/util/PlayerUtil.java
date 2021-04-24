package vip.creatio.clib.basic.util;

import io.netty.channel.Channel;
import net.minecraft.server.PlayerConnection;
import vip.creatio.clib.basic.packet.Packet;
import org.bukkit.entity.Player;

public final class PlayerUtil {

    private PlayerUtil() {}

    public static void sendPacket(Player plr, Packet<?> packet) {
        getConnection(plr).sendPacket(packet.unwrap());
    }

    public static PlayerConnection getConnection(Player plr) {
        return EntityUtil.toNms(plr).playerConnection;
    }

    public static Channel getChannel(Player plr) {
        return getConnection(plr).networkManager.channel;
    }

}
