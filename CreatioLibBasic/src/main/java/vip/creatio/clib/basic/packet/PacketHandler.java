package vip.creatio.clib.basic.packet;

import org.bukkit.entity.Player;

/**
 * Light-weight packet event handler, used in PacketListener to register packet handler
 * @param <T>
 */
public interface PacketHandler<T extends Packet<?>> {

    boolean handle(T packet, Player associated);

}
