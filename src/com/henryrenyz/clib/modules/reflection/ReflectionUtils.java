package com.henryrenyz.clib.modules.reflection;

import com.henryrenyz.clib.modules.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    public static String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    public static Class<?> getCBClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }

    //get a variable from a instance by reflection
    public static Object getVariable(Class<?> clazz, String varname, Object instance) {
        try {
            Field f = clazz.getDeclaredField(varname);
            f.setAccessible(true);
            return f.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //get a annotation by reflection
    public static Annotation getAnnotation(Class<?> clazz, Class<Annotation> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    //Primitive conversion
    static Class<?> toPrimitive(Class<?> wrapClass) {
        switch (wrapClass.getCanonicalName()) {
            case "java.lang.Byte":
                return byte.class;
            case "java.lang.Integer":
                return int.class;
            case "java.lang.Short":
                return short.class;
            case "java.lang.Long":
                return long.class;
            case "java.lang.Float":
                return float.class;
            case "java.lang.Double":
                return double.class;
            case "java.lang.Boolean":
                return boolean.class;
            case "java.lang.Character":
                return char.class;
            case "java.lang.Byte[]":
                return byte[].class;
            case "java.lang.Integer[]":
                return int[].class;
            case "java.lang.Short[]":
                return short[].class;
            case "java.lang.Long[]":
                return long[].class;
            case "java.lang.Float[]":
                return float[].class;
            case "java.lang.Double[]":
                return double[].class;
            case "java.lang.Boolean[]":
                return boolean[].class;
            case "java.lang.Character[]":
                return char[].class;
        }
        return wrapClass;
    }

    //Bukkit Player -> NMS Entity Player
    public static Object getNMSPlayer(@NotNull Player p) throws InvocationTargetException, IllegalAccessException {
        return ReflectionMethod.CraftPlayer_getHandle.m.invoke(p);
    }

    //Bukkit Material -> new NMS Item
    public static Object getNMSItem(@NotNull Material item) throws InvocationTargetException, IllegalAccessException {
        return ReflectionMethod.ItemStack_getItem.m.invoke(ReflectionMethod.CraftItemStack_asNMSCopy.m.invoke(new ItemStack(item)));
    }

    //Bukkit ItemStack -> NMS ItemStack
    public static Object getNMSItemStack(@NotNull ItemStack item) throws InvocationTargetException, IllegalAccessException {
        return ReflectionField.CraftItemStack_handle.f.get(item);
    }

    //Bukkit ItemStack -> clone NMS ItemStack
    public static Object getNMSItemStackCopy(@NotNull ItemStack item) throws InvocationTargetException, IllegalAccessException {
        return ReflectionMethod.CraftItemStack_asNMSCopy.m.invoke(null, item);
    }

    //NMS NBTTagCompound -> new MNS ItemStack
    public static Object getNMSItemStack(@NotNull Object nbtTagCompound) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return ReflectionClass.ItemStack.c.getMethod("a", ReflectionClass.NBTTagCompound.c).invoke(null, nbtTagCompound);
    }

    //NMS ItemStack -> new Bukkit ItemStack
    public static Object getBukkitItemStack(@NotNull Object item) throws InvocationTargetException, IllegalAccessException {
        return ReflectionMethod.CraftItemStack_asBukkitCopy.m.invoke(null, item);
    }

    //Bukkit ItemStack -> CraftItemStack
    public static Object getCraftItemStack(@NotNull ItemStack item) {
        return ReflectionClass.CraftItemStack.c.cast(item);
    }

    //Bukkit ItemMeta -> CraftMetaItem
    public static Object getCraftItemMeta(@NotNull ItemMeta meta) {
        return ReflectionClass.CraftMetaItem.c.cast(meta);
    }

    //Bukkit BlockData -> NMS BlockData
    public static Object getNMSBlockData(@NotNull BlockData data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return ReflectionClass.CraftBlockData.c.cast(data).getClass().getMethod("getState").invoke(ReflectionClass.CraftBlockData.c.cast(data));
    }

    //Sting NBT -> NMS NBT
    public static Object getNMSNBT(@NotNull String nbt) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringUtil.checkDepth(nbt);
        return ReflectionClass.MojangsonParser.c.getMethod("parse", String.class).invoke(null, nbt);
    }

    //String Minecraft Item Namespace -> Bukkit Material
    public static Material getMaterial(@NotNull String nameSpace) {
        Object m = ReflectionConstructor.run(ReflectionConstructor.MinecraftKey.c, nameSpace);                                             //Get MinecraftKey Object
        Object d = ReflectionMethod.run(ReflectionClass.IRegistry.c, "get", ReflectionField.IRegistry_Item.getVar(), m);           //Get Item Object from MinecraftKey using IRegistry Item getter
        ItemStack item = (ItemStack) ReflectionMethod.run(ReflectionMethod.CraftItemStack_asNewCraftStack.m, null, d);             //Get Bukkit Itemstack from Minecraft Item through asCraftCopy method
        assert item != null;
        return item.getType();
    }
}

