package vip.creatio.clib.basic.tools;

import vip.creatio.clib.basic.CLibBasic;
import vip.creatio.clib.basic.annotation.Listener;
import vip.creatio.common.ReflectUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

public class ListenerManager implements ListenerRegister {

    private final JavaPlugin plugin;

    public ListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public <T extends Event> void register(Class<T> clazz, Consumer<T> call, EventPriority priority, boolean ignoresCancelled) {
        @SuppressWarnings("unchecked")
        EventExecutor exec = (l, e) -> {
            if (!clazz.isAssignableFrom(e.getClass())) {
                return;
            }
            ((Consumer<Event>) call).accept(e);
        };
        org.bukkit.event.Listener l = new org.bukkit.event.Listener() {};

        Bukkit.getPluginManager().registerEvent(clazz, l, priority, exec, plugin, ignoresCancelled);
    }

    public void register(@NotNull Class<? extends Event> clazz, @NotNull Method mth, @NotNull org.bukkit.event.Listener listener) {
        try {
            if (       mth.getParameterCount() == 1
                    //Should returns void
                    && mth.getReturnType() == void.class
                    //Should have Event parameter
                    && mth.getParameterTypes()[0] == clazz
                    //Should be non-private method
                    && !Modifier.isPrivate(mth.getModifiers())) {
                mth.setAccessible(true);

                MethodHandles.Lookup lookup = ReflectUtil.lookupIn(mth.getDeclaringClass());

                MethodHandle b = lookup.unreflect(mth);

                EventPriority p;
                boolean ignoresCancelled;

                EventHandler handler = mth.getAnnotation(EventHandler.class);
                if (handler != null) {
                    p = handler.priority();
                    ignoresCancelled = handler.ignoreCancelled();
                } else {
                    Listener listener1 = mth.getAnnotation(Listener.class);
                    if (listener1 != null) {
                        p = listener1.priority();
                        ignoresCancelled = listener1.ignoreCancelled();
                    } else return;
                }

                // If is a static method, then use a Consumer instead of EventExecutor
                if (Modifier.isStatic(mth.getModifiers())) {
                    @SuppressWarnings("unchecked")
                    Consumer<Event> exec = (Consumer<Event>) LambdaMetafactory.metafactory(
                            lookup,
                            "accept",
                            MethodType.methodType(Consumer.class),
                            MethodType.methodType(void.class, Object.class),
                            b,
                            b.type()
                    ).getTarget().invokeExact();

                    Bukkit.getPluginManager().registerEvent(
                            clazz,
                            listener,
                            p,
                            (l, e) -> {
                                if (!clazz.isAssignableFrom(e.getClass())) {
                                    return;
                                }
                                exec.accept(e);
                            },
                            plugin,
                            ignoresCancelled);
                } else {
                    EventExecutor exec = (EventExecutor) LambdaMetafactory.metafactory(
                            lookup,
                            "execute",
                            MethodType.methodType(EventExecutor.class),
                            MethodType.methodType(void.class, org.bukkit.event.Listener.class, Event.class),
                            b,
                            b.type()
                    ).getTarget().invokeExact();

                    Bukkit.getPluginManager().registerEvent(
                            clazz,
                            listener,
                            p,
                            (l, e) -> {
                                if (!clazz.isAssignableFrom(e.getClass())) {
                                    return;
                                }
                                exec.execute(l, e);
                            },
                            plugin,
                            ignoresCancelled);
                }
            }
        } catch (Throwable t) {
            CLibBasic.intern("Failed to register listener class " + mth.getDeclaringClass() + "! ");
            t.printStackTrace();
        }
    }

}
