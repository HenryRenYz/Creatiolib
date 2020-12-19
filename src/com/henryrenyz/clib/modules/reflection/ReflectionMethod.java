package com.henryrenyz.clib.modules.reflection;

import com.henryrenyz.clib.modules.Message;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.henryrenyz.clib.modules.reflection.ReflectionClass.*;

/**
 * An enum of reflection method, basically reflected from
 * net.minecraft.server package and craftbukkit package.
 *
 * Get the method of an enum: etc. XXX.m
 *
 * @variable m the Method represented by enum.
 */
public enum ReflectionMethod {

    NBTTagCompound_set(NBTTagCompound, "set", String.class, NBTBase.c),
    NBTTagCompound_hasKey(NBTTagCompound, "hasKey", String.class),
    NBTTagList_add(NBTTagList, "add", int.class, NBTBase.c),
    NBTTagList_set(NBTTagList, "set", int.class, NBTBase.c),

    ItemStack_getItem(ItemStack, "getItem"),
    ItemStack_getTag(ItemStack, "getTag"),
    PlayerConnection_sendPacket(PlayerConnection, "sendPacket", Packet.c),

    CraftServer_getCommandMap(CraftServer, "getCommandMap"),
    CraftItemStack_asNMSCopy(CraftItemStack, "asNMSCopy", ItemStack.class),
    CraftItemStack_asBukkitCopy(CraftItemStack, "asBukkitCopy", ItemStack.c),
    CraftItemStack_asNewCraftStack(CraftItemStack, "asNewCraftStack", Item.c),
    CraftPlayer_getHandle(CraftPlayer, "getHandle"),
    ;

    public final Method m;

    ReflectionMethod(Method method) {
        this.m = method;
    }

    ReflectionMethod(ReflectionClass refClass, String method, Class<?>... params) {
        this(refClass.c, method, params);
    }
    ReflectionMethod(Class<?> clazz, String method, Class<?>... params) {
        Method m;
        try {
            m = clazz.getDeclaredMethod(method, params);
            m.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Message.debug("&4Registration of method &6&l" + method + "&4 failed!");
            e.printStackTrace();
            m = null;
        }
        this.m = m;
    }

    /**
     * Run a method of a class.
     * The method will be automatically selected using types of param.
     * Number, Char, and Boolean will be converted to their primitive.
     *
     * @param clazz the class to which the method belongs.
     *
     * @param method the name of method.
     *
     * @param member the member to be invoked if the method is not static.
     *
     * @param args the params.
     *
     * @return java.lang.Object
     */
    public static Object run(Class<?> clazz, String method, Object member, Object... args) {
        try {
            if (args[0] == null) {
                return get(clazz, method).invoke(member);
            } else {
                List<Class<?>> list = new ArrayList<>();
                for (Object c : args) {
                    list.add(ReflectionUtils.toPrimitive(c.getClass()));
                }
                return get(clazz, method, list.toArray(new Class[0])).invoke(member, args);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Message.debug("&4Execution of method from string &6&l" + method + "&4 failed!");
            e.printStackTrace();
            return null;
        }
    }
    public static Object run(ReflectionClass refClass, String method, Object member, Object... args) {
        return run(refClass.c, method, member, args);
    }


    public static Object run(Class<?> clazz, String method, Object member) {
        return run(clazz, method, member, (Object) null);
    }
    public static Object run(ReflectionClass refClass, String method, Object member) {
        return run(refClass.c, method, member, (Object) null);
    }

    /**
     * Run a method of a class using given method.
     *
     * @param method the method in a class.
     *
     * @param member the member to be invoked if the method is not static.
     *
     * @param args the params.
     *
     * @return java.lang.Object
     */
    public static Object run(Method method, Object member, Object... args) {
        try {
            return method.invoke(member, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            Message.debug("&4Execution of method &6&l" + method.getName() + "&4 failed!");
            e.printStackTrace();
            return null;
        }
    }
    public static Object run(ReflectionMethod refMethod, Object member, Object... args) {
        return run(refMethod.m, member, args);
    }
    public Object run(Object member, Object... args) {
        return run(this.m, member, args);
    }

    /**
     * Get a method of a class.
     *
     * This automatically bypass the access check.
     *
     * @param clazz the class to which the method belongs.
     *
     * @param method the name of method.
     *
     * @throws NoSuchMethodException if method does not exist.
     *
     * @return java.lang.Object
     */
    public static Method get(Class<?> clazz, String method) throws NoSuchMethodException {
        Method m = clazz.getDeclaredMethod(method);
        m.setAccessible(true);
        return m;
    }
    public static Method get(ReflectionClass refClass, String method) throws NoSuchMethodException {
        return get(refClass.c, method);
    }

    /**
     * Get a method of a class with specific classes of param.
     *
     * This automatically bypass the access check.
     *
     * @param clazz the class to which the method belongs.
     *
     * @param method the name of method.
     *
     * @param argsClass the classes of param.
     *
     * @throws NoSuchMethodException if method does not exist.
     *
     * @return java.lang.Object
     */
    public static Method get(Class<?> clazz, String method, Class<?>... argsClass) throws NoSuchMethodException {
        Method m = clazz.getDeclaredMethod(method, argsClass);
        m.setAccessible(true);
        return m;
    }
    public static Method get(ReflectionClass refClass, String method, Class<?>... argsClass) throws NoSuchMethodException {
        return get(refClass.c, method, argsClass);
    }
}
