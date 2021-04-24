package vip.creatio.clib.basic.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import vip.creatio.clib.basic.util.PlayerUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelPacketListener implements PacketListener {

    private static final String PACKET_HANDLER = "packet_handler";

    private final Map<Player, PerPlayerHandler> playerHandlerMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends net.minecraft.server.Packet<?>>, List<PacketHandler<?>>> handlerIn = new ConcurrentHashMap<>();
    private final Map<Class<? extends net.minecraft.server.Packet<?>>, List<PacketHandler<?>>> handlerOut = new ConcurrentHashMap<>();
    private final String registerName;

    public ChannelPacketListener(String name) {
        this.registerName = name;
    }

    /** When server ready to send a packet, return false to cancel this event */
    @SuppressWarnings("unchecked")
    protected boolean onSend(Player p, net.minecraft.server.Packet<?> nmsPacket) {
        List<PacketHandler<?>> handlers = handlerOut.get(nmsPacket.getClass());
        if (handlers != null) {
            for (PacketHandler<?> h : handlers) {
                try {
                    if (!((PacketHandler<Packet<?>>) h).handle(Packet.wrap(nmsPacket), p)) {
                        return false;
                    }
                } catch (Throwable t) {
                    System.err.println("Packet handler " + h + " unable to handle O packet " + nmsPacket + ": ");
                    t.printStackTrace();
                }
            }
        }
        return true;
    }

    /** When server receive a packet from client, return false to cancel this event */
    @SuppressWarnings("unchecked")
    protected boolean onReceive(Player p, net.minecraft.server.Packet<?> nmsPacket) {
        List<PacketHandler<?>> handlers = handlerIn.get(nmsPacket.getClass());
        if (handlers != null) {
            for (PacketHandler<?> h : handlers) {
                try {
                    if (!((PacketHandler<Packet<?>>) h).handle(Packet.wrap(nmsPacket), p)) {
                        return false;
                    }
                } catch (Throwable t) {
                    System.err.println("Packet handler " + h + " unable to handle I packet " + nmsPacket + ": ");
                    t.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void addPlayer(Player p) {
        playerHandlerMap.put(p, new PerPlayerHandler(p));
    }

    @Override
    public void removePlayer(Player p) {
        playerHandlerMap.remove(p);
    }

    @Override
    public void close() {
        for (PerPlayerHandler handler : playerHandlerMap.values()) {
            try {
                handler.channel.pipeline().remove(handler);
            } catch (NoSuchElementException ignored) { }
        }
        unregisterAll();
    }

    @Override
    public <T extends Packet<?>> void register(Class<T> packetClass, PacketHandler<T> handler) {
        Class<? extends net.minecraft.server.Packet<?>> pkNms = Packet.internalGetNmsClassIn(packetClass);
        List<PacketHandler<?>> list;
        if (pkNms != null) {
            list = handlerIn.computeIfAbsent(pkNms, k -> Collections.synchronizedList(new ArrayList<>()));
        } else {
            pkNms = Packet.internalGetNmsClassOut(packetClass);
            if (pkNms != null) {
                list = handlerOut.computeIfAbsent(pkNms, k -> Collections.synchronizedList(new ArrayList<>()));
            } else {
                throw new RuntimeException("Unknown packet " + packetClass);
            }
        }
        list.add(handler);
    }

    @Override
    public <T extends Packet<?>> void unregister(Class<T> packetClass, PacketHandler<T> handler) {
        List<PacketHandler<?>> list = handlerIn.get(packetClass);
        if (list != null) {
            list.removeIf(packetHandler -> packetHandler.equals(handler));
            if (list.size() == 0) handlerIn.remove(packetClass);
        } else {
            list = handlerOut.get(packetClass);
            if (list != null) {
                list.removeIf(packetHandler -> packetHandler.equals(handler));
                if (list.size() == 0) handlerOut.remove(packetClass);
            } else {
                throw new RuntimeException("Unknown packet " + packetClass);
            }
        }
    }

    @Override
    public void unregister(Class<? extends Packet<?>> packetClass) {
        handlerIn.remove(packetClass);
        handlerOut.remove(packetClass);
    }

    @Override
    public void unregister(PacketHandler<?> handler) {
        for (List<PacketHandler<?>> l : handlerIn.values()) {
            l.removeIf(packetHandler -> packetHandler.equals(handler));
        }
        for (List<PacketHandler<?>> l : handlerOut.values()) {
            l.removeIf(packetHandler -> packetHandler.equals(handler));
        }
    }

    @Override
    public void unregisterAll() {
        handlerIn.clear();
        handlerOut.clear();
    }

    @Override
    public List<PacketHandler<?>> getHandlers() {
        List<PacketHandler<?>> handlers = new ArrayList<>();
        for (List<PacketHandler<?>> l : handlerIn.values()) {
            handlers.addAll(l);
        }
        for (List<PacketHandler<?>> l : handlerOut.values()) {
            handlers.addAll(l);
        }
        return handlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Packet<?>> List<PacketHandler<T>> getHandlers(Class<T> packetClass) {
        List<PacketHandler<T>> list = (List) handlerIn.get(packetClass);
        if (list == null) {
            list = (List) handlerOut.get(packetClass);
            if (list == null)
                return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ChannelPacketListener{name:").append(registerName).append(", Player:").append(playerHandlerMap.keySet()).append(", I:");
        for (Map.Entry<Class<? extends net.minecraft.server.Packet<?>>, List<PacketHandler<?>>> entry
                : handlerIn.entrySet()) {
            sb.append(entry.getKey().getSimpleName()).append(':').append(entry.getValue()).append(", ");
        }
        sb.append("O:");
        for (Map.Entry<Class<? extends net.minecraft.server.Packet<?>>, List<PacketHandler<?>>> entry
                : handlerOut.entrySet()) {
            sb.append(entry.getKey().getSimpleName()).append(':').append(entry.getValue()).append(", ");
        }
        sb.append('}');
        return sb.toString();
    }

    private class PerPlayerHandler extends ChannelDuplexHandler {

        private final Player player;
        private final Channel channel;

        PerPlayerHandler(Player p) {
            this.player = p;
            this.channel = PlayerUtil.getChannel(p);
            this.channel.pipeline().addBefore(PACKET_HANDLER, registerName, this);
        }

        @Override
        public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            boolean willSend = true;
            if (msg instanceof net.minecraft.server.Packet) {
                willSend = ChannelPacketListener.this.onSend(player, (net.minecraft.server.Packet<?>) msg);
            }
            if (willSend) super.write(ctx, msg, promise);
        }

        @Override
        public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            boolean willSend = true;
            if (msg instanceof net.minecraft.server.Packet) {
                willSend = ChannelPacketListener.this.onReceive(player, (net.minecraft.server.Packet<?>) msg);
            }
            if (willSend) super.channelRead(ctx, msg);
        }
    }
}
