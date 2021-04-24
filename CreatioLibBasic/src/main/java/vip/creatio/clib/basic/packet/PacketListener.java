package vip.creatio.clib.basic.packet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import vip.creatio.clib.basic.annotation.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface PacketListener {

    Map<Plugin, List<PacketListener>> LISTENERS = new ConcurrentHashMap<>();

    void addPlayer(Player p);

    void removePlayer(Player p);

    <T extends Packet<?>> void register(Class<T> packetClass, PacketHandler<T> handler);

    <T extends Packet<?>> void unregister(Class<T> packetClass, PacketHandler<T> handler);

    void unregister(Class<? extends Packet<?>> packetClass);

    void unregister(PacketHandler<?> handler);

    void unregisterAll();

    void close();

    List<PacketHandler<?>> getHandlers();

    <T extends Packet<?>> List<PacketHandler<T>> getHandlers(Class<T> packetClass) ;

    @Listener
    static void onJoin(PlayerJoinEvent event) {
        for (List<PacketListener> l : LISTENERS.values()) {
            for (PacketListener pl : l) {
                pl.addPlayer(event.getPlayer());
            }
        }
    }

    @Listener
    static void onQuit(PlayerQuitEvent event) {
        for (List<PacketListener> l : LISTENERS.values()) {
            for (PacketListener pl : l) {
                pl.removePlayer(event.getPlayer());
            }
        }
    }

    static void registerListener(Plugin p, PacketListener listener) {
        List<PacketListener> pl = LISTENERS.computeIfAbsent(p, x -> new ArrayList<>());
        pl.add(listener);
        for (Player player : Bukkit.getOnlinePlayers()) {
            listener.addPlayer(player);
        }
    }

    static void unregisterListener(Plugin p, PacketListener listener) {
        List<PacketListener> pl = LISTENERS.get(p);
        if (pl != null) {
            boolean succeed = pl.remove(listener);
            if (succeed) listener.close();
        }
    }

    static void unregisterListeners(Plugin p) {
        List<PacketListener> pl = LISTENERS.remove(p);
        if (pl != null) {
            for (PacketListener l : pl) {
                l.close();
            }
        }
    }

    static void unregisterListeners() {
        for (List<PacketListener> l : LISTENERS.values()) {
            for (PacketListener pl : l) {
                pl.close();
            }
        }
        LISTENERS.clear();
    }
}
