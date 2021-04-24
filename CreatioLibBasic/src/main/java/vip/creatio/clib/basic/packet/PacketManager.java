package vip.creatio.clib.basic.packet;

import vip.creatio.common.Mth;
import vip.creatio.common.Pair;
import vip.creatio.clib.basic.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketManager {

    private static final Map<Player, Pair<Packet.Priority, Integer>> PRIORITY_MAP = new ConcurrentHashMap<>();

    public PacketManager() {
        //TODO: add a packet listener
    }

    //Send packet
    public void send(Packet<?> packet, Player @NotNull ... p) {
        for (Player player : p) {
            sendPacket(packet, player);
        }
    }

    //Send packet to all players in world(s)
    public void send(Packet<?> packet, @NotNull World world) {
        for (Player p : world.getPlayers()) {
            sendPacket(packet, p);
        }
    }

    //Send packet to all players in current server
    public void send(@NotNull Packet<?> packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendPacket(packet, p);
        }
    }

    public void sendPacket(@NotNull Packet<?> packet, Player p) {
        boolean willSend = false;
        if (packet.getPriority() == Packet.Priority.HIGH) {
            willSend = true;
        } else {
            Pair<Packet.Priority, Integer> pair = PRIORITY_MAP.get(p);

            if (pair == null) {
                pair = new Pair<>(Packet.Priority.HIGH, 0);
                PRIORITY_MAP.put(p, pair);
            }

            if (pair.getKey() == Packet.Priority.HIGH) {
                // High priority
                willSend = true;
            } else if (pair.getKey() == Packet.Priority.NORMAL) {
                // Normal priority
                if (packet.getPriority() == Packet.Priority.NORMAL) {
                    if (Mth.within(pair.getValue() % 3, 1, 2)) {
                        willSend = true;
                    }
                } else {
                    if (pair.getValue() % 3 == 0) {
                        willSend = true;
                    }
                }
            } else {
                // Low priority
                if (packet.getPriority() == Packet.Priority.NORMAL) {
                    if (pair.getValue() % 3 == 1) {
                        willSend = true;
                    }
                } else {
                    if (pair.getValue() % 6 == 1) {
                        willSend = true;
                    }
                }
            }
        }
        if (willSend)
            PlayerUtil.sendPacket(p, packet);
    }
}
