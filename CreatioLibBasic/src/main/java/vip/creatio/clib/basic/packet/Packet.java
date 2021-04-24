package vip.creatio.clib.basic.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import vip.creatio.accessor.Func;
import vip.creatio.accessor.Reflection;
import vip.creatio.clib.basic.CLibBasic;
import vip.creatio.clib.basic.internal.CLibBasicBootstrap;
import vip.creatio.clib.basic.internal.Wrapped;
import vip.creatio.clib.basic.tools.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import vip.creatio.common.ReflectionException;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Wrapper class of NMS Packet, with some additional utils.
 *
 * About final fields in specific packets: these are structural
 * or time-consuming to change, with means changing these fields
 * are basically no difference than creating a new instance.
 *
 * About the use of abstract class rather than interface:
 * Java compiler report error when an interface implements another
 * interface with a non-exist generic, but abstract class doesn't have
 * this kind of issue.
 */
@SuppressWarnings("unchecked")
public abstract class Packet<T extends net.minecraft.server.Packet<?>> implements Wrapper<T> {

    private static final PacketManager MANAGER = CLibBasic.getInstance().getPacketManager();

    private static final BiMap<Class<? extends net.minecraft.server.Packet<?>>, Class<? extends Packet<?>>> IN_PACKET =
            HashBiMap.create();

    private static final BiMap<Class<? extends net.minecraft.server.Packet<?>>, Class<? extends Packet<?>>> OUT_PACKET =
            HashBiMap.create();

    private static final Map<Class<? extends net.minecraft.server.Packet<?>>, Func<? extends Packet<?>>> WRAPPER = new HashMap<>();

    static {
        CLibBasicBootstrap bootstrap = CLibBasic.getInstance().getBootstrap();
        BiConsumer<List<Class<?>>, BiMap<Class<? extends net.minecraft.server.Packet<?>>, Class<? extends Packet<?>>>>
                addClass = (l, b) ->
        {
            for (Class<?> pk : l) {

                Wrapped anno = pk.getAnnotation(Wrapped.class);
                Class<?> nmsClass;
                if (anno != null) {
                    // Get class through Wrapped annotation
                    nmsClass = anno.value();
                } else try {
                    // Else try to get class from Packet generic parameter
                    if (pk.getSuperclass() != Packet.class
                        || Modifier.isAbstract(pk.getModifiers()))
                        continue;

                    String clsName = pk.getGenericSuperclass().getTypeName().split("<")[1];
                    if (clsName.contains("extends") || clsName.contains("super")) continue;

                    nmsClass = Class.forName(clsName.substring(0, clsName.length() - 1));
                } catch (ClassNotFoundException ignored) {
                    // If noting found then this class probably not a packet class
                    continue;
                }

                // Put class instance to map
                b.put((Class<? extends net.minecraft.server.Packet<?>>) nmsClass, (Class<? extends Packet<?>>) pk);

                // Get wrapper constructor
                try {
                    WRAPPER.put((Class<? extends net.minecraft.server.Packet<?>>) nmsClass,
                            Reflection.constructor((Class<Packet<?>>) pk, nmsClass));
                } catch (ReflectionException e) {
                    if (e.getCause() instanceof NoSuchMethodException) {
                        CLibBasic.intern("Can't find wrapper constructor in class " + pk.getName() + "!");
                    } else {
                        e.getCause().printStackTrace();
                    }
                }
            }
        };
        addClass.accept(bootstrap.getClassUnder("vip.creatio.clib.basic.packet.in"), IN_PACKET);
        addClass.accept(bootstrap.getClassUnder("vip.creatio.clib.basic.packet.out"), OUT_PACKET);
    }

    /** Used to convert packet */
    protected static final PacketByteBuf BUFFER = new PacketByteBuf(1024);

    public static <T extends net.minecraft.server.Packet<?>> Packet<T> wrap(T raw) {
        Func<Packet<T>> c = (Func<Packet<T>>) WRAPPER.get(raw.getClass());
        if (c == null) {
            throw new NoSuchMethodError("This packet class (" + raw.getClass().getName() + ") doesn't have a wrapper class yet!");
        }
        return c.invoke(raw);
    }

    public static Class<?> getNmsClass(Class<? extends Packet<?>> wrapped) {
        return IN_PACKET.inverse().get(wrapped);
    }

    public static Class<? extends Packet<?>> getWrappedClass(Class<?> nms) {
        return IN_PACKET.get(nms);
    }

    protected final T original;
    protected Priority priority = Priority.NORMAL;

    protected Packet(T nms) {
        this.original = nms;
    }

    protected void fillBuf() {
        try {
            BUFFER.clear();
            write(BUFFER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read(PacketByteBuf buf) throws IOException {
        original.a /* read */ (buf.unwrap());
    }

    public void write(PacketByteBuf buf) throws IOException {
        original.b /* write */ (buf.unwrap());
    }

    //Send packet
    public void send(Player... p) {
        for (Player player : p) {
            sendTo(player);
        }
    }

    //Send packet to all players in world(s)
    public void send(@NotNull World world) {
        for (Player p : world.getPlayers()) {
            sendTo(p);
        }
    }

    //Send packet to all players in current server
    public void send() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendTo(p);
        }
    }

    public void sendTo(@NotNull Player p) {
        MANAGER.sendPacket(this, p);
    }

    @Override
    public T unwrap() {
        return original;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends T> wrappedClass() {
        return (Class<? extends T>) original.getClass();
    }

    public Priority getPriority() {
        return priority;
    }

    public Packet<T> setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }


    public enum Priority {
        LOW,        // 1/3
        NORMAL,     // 2/3
        HIGH        // 3/3
    }


    // INTERNAL //

    static <T extends net.minecraft.server.Packet<?>> Class<T> internalGetNmsClassIn(Class<?> wrapped) {
        return (Class<T>) IN_PACKET.inverse().get(wrapped);
    }

    static <T extends net.minecraft.server.Packet<?>> Class<? extends Packet<T>> intergalGetWrappedClassIn(Class<?> wrapped) {
        return (Class<? extends Packet<T>>) IN_PACKET.get(wrapped);
    }

    static <T extends net.minecraft.server.Packet<?>> Class<T> internalGetNmsClassOut(Class<?> wrapped) {
        return (Class<T>) OUT_PACKET.inverse().get(wrapped);
    }

    static <T extends net.minecraft.server.Packet<?>> Class<? extends Packet<T>> intergalGetWrappedClassOut(Class<?> wrapped) {
        return (Class<? extends Packet<T>>) OUT_PACKET.get(wrapped);
    }
}
