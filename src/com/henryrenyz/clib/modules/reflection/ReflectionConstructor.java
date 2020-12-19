package com.henryrenyz.clib.modules.reflection;

import com.henryrenyz.clib.modules.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * An enum of reflection constructor, basically reflected from
 * net.minecraft.server package and craftbukkit package.
 *
 * Get the constructor of an enum: etc. XXX.c
 *
 * @variable c the Constructor represented by enum.
 */

public enum ReflectionConstructor {

    MinecraftKey(ReflectionClass.MinecraftKey.c, String.class),
    PluginCommand("org.bukkit.command.PluginCommand", String.class, Plugin.class),
    ;

    public Constructor<?> c;

    ReflectionConstructor(Class<?> clazz, Class<?>... params) {
        try {
            Constructor<?> c = clazz.getDeclaredConstructor(params);
            c.setAccessible(true);
            this.c = c;
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().warning("&8&l[&b&lCreatio&3&lLib&8&l] &4Registration of constructor of class &6&l" + clazz.getSimpleName() + "&4 failed!");
            e.printStackTrace();
            this.c = null;
        }
    }

    ReflectionConstructor(ReflectionClass refClass, Class<?>... params) {
        this(refClass.c, params);
    }

    ReflectionConstructor(String rawClass, Class<?>... params) {
        this();
        try {
            Constructor<?> c = Class.forName(rawClass).getDeclaredConstructor(params);
            c.setAccessible(true);
            this.c = c;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            Message.debug("&4Registration of constructor of class from string &6&l" + rawClass + "&4 failed!");
        }
    }

    ReflectionConstructor() {this.c = null;}

    /**
     * Run a constructor from a class using specific types of param.
     * The constructor will be automatically selected using types of param.
     * Number, Char, and Boolean will be converted to their primitive.
     *
     * This automatically bypass the access check.
     *
     * Return an instance created by the constructor as an object
     *
     * @param clazz the class to which constructor belongs.
     *
     * @param args params to be used in the constructor.
     *
     * @return Object
     */
    public static Object run(Class<?> clazz, Object... args) {
        try {
            if (args.length == 1) {
                return get(clazz).newInstance(args);
            } else {
                List<Class<?>> list = new ArrayList<>();
                for (Object o : args) {
                    list.add(ReflectionUtils.toPrimitive(o.getClass()));
                }
                return get(clazz, list.toArray(new Class[0])).newInstance(args);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            Message.debug("&4Construction of instance from class &6&l" + clazz.getSimpleName() + "&4 failed!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Run a constructor with a known constructor.
     * This can be used for constructors with complicated param types.
     * The preset constructor do not require param analyser, which is
     * faster than the one above.
     *
     * This automatically bypass the access check.
     *
     * Return an instance created by the constructor as an object
     *
     * @param constructor the constructor of a class.
     *
     * @param args params to be used in the constructor.
     *
     * @return java.lang.Object
     */
    public static Object run(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            Message.debug("&4Construction of instance using constructor &6&l" + constructor.getName() + "&4 failed!");
            e.printStackTrace();
            return null;
        }
    }
    public static Object run(ReflectionConstructor refConstructor, Object... args) {
        return run(refConstructor.c, args);
    }
    public Object run(Object... args) {
        return run(this.c, args);
    }

    /**
     * Get the no-param constructor of a class.
     *
     * This automatically bypass the access check.
     *
     * @param clazz the class to which constructor belongs.
     *
     * @throws NoSuchMethodException if no-param constructor does not exist.
     *
     * @return java.lang.reflect.Constructor
     */
    public static Constructor<?> get(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> c = clazz.getDeclaredConstructor();
        c.setAccessible(true);
        return c;
    }

    /**
     * Get the constructor with specific param type from a class.
     *
     * This automatically bypass the access check.
     *
     * @param clazz the class to which constructor belongs.
     *
     * @param argsClass the class(es) of param types.
     *
     * @throws NoSuchMethodException if constructor does not exist.
     *
     * @return java.lang.reflect.Constructor
     */
    public static Constructor<?> get(Class<?> clazz, Class<?>... argsClass) throws NoSuchMethodException {
        Constructor<?> c = clazz.getDeclaredConstructor(argsClass);
        c.setAccessible(true);
        return c;
    }
}
