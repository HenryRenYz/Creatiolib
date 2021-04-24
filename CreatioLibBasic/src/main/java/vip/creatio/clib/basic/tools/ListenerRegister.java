package vip.creatio.clib.basic.tools;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public interface ListenerRegister {

    <T extends Event> void register(Class<T> clazz, Consumer<T> call, EventPriority priority, boolean ignoresCancelled);

    void register(@NotNull Class<? extends Event> clazz, @NotNull Method mth, @NotNull org.bukkit.event.Listener listener);

    default void register(@NotNull Class<? extends Event> clazz, @NotNull Method mth) {
        register(clazz, mth, new org.bukkit.event.Listener() {});
    }

    default void register(@NotNull Method mth, @NotNull org.bukkit.event.Listener listener) {
        register((Class<? extends Event>) mth.getParameterTypes()[0], mth, listener);
    }

    default void register(@NotNull Method mth) {
        register((Class<? extends Event>) mth.getParameterTypes()[0], mth, new org.bukkit.event.Listener() {});
    }

    default <T extends Event> void register(@NotNull Class<T> clazz, @NotNull Consumer<T> call, @NotNull EventPriority priority) {
        register(clazz, call, priority, false);
    }

    default <T extends Event> void register(@NotNull Class<T> clazz, @NotNull Consumer<T> call) {
        register(clazz, call, EventPriority.NORMAL);
    }

    default void register(@NotNull Class<? extends org.bukkit.event.Listener> cls) {
        try {
            Constructor<Listener> constructor = (Constructor<org.bukkit.event.Listener>) cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            org.bukkit.event.Listener listener = constructor.newInstance();

            for (Method m : cls.getDeclaredMethods()) {
                Class<?> eventClass = m.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(eventClass)) {
                    register((Class<? extends Event>) eventClass, m, listener);
                }
            }
        } catch (Throwable e) {
            System.err.println("Failed to register listener class " + cls + "! ");
            e.printStackTrace();
        }
    }
}
