package com.henryrenyz.clib.modules.reflection;

import com.henryrenyz.clib.modules.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;;

import java.lang.reflect.Field;

/**
 * An enum of reflection field, basically reflected from
 * net.minecraft.server package and craftbukkit package.
 *
 * Get the field of an enum: etc. XXX.f
 *
 * Get the variable of the field: etc. XXX.geVar(member)
 *
 * @variable f Field represented by enum.
 */

public enum ReflectionField {

    IRegistry_Item(ReflectionClass.IRegistry.c, "ITEM"),
    EntityPlayer_playerConnection(ReflectionClass.EntityPlayer.c, "playerConnection"),
    CraftItemStack_handle(ReflectionClass.CraftItemStack.c, "handle"),
    CraftMetaItem_internalTag(ReflectionClass.CraftMetaItem.c, "internalTag"),
    ;

    public Field f;
    ReflectionField(Field Field) {
        this.f = Field;
    }

    ReflectionField(Class<?> clazz, String name) {
        Field c = null;
        try {
            c = clazz.getDeclaredField(name);
            c.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Message.debug("&4Registration of field &6&l" + name + "&4 failed!");
            e.printStackTrace();
        }
        this.f = c;
    }

    /**
     * Get the variable of a field.
     *
     * No try catch needed.
     *
     * @param member the member to which variable belongs.
     *
     * @return java.lang.Object
     */
    public @Nullable Object getVar(Object member) {
        try {
            return this.f.get(member);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public @Nullable Object getVar() {
        try {
            return this.f.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the Field of a class.
     *
     * This automatically bypass the access check.
     *
     * @param clazz the class to which Field belongs.
     *
     * @throws NoSuchFieldException if the Field does not exist.
     *
     * @return java.lang.reflect.Field
     */
    public static @NotNull Field get(@NotNull Class<?> clazz, String name) throws NoSuchFieldException {
        Field c = clazz.getDeclaredField(name);
        c.setAccessible(true);
        return c;
    }

    /**
     * Get a variable from a class.
     *
     * This automatically bypass the access check.
     *
     * @param clazz the class to which the variable belongs.
     *
     * @param name the name of variable
     *
     * @param member the member to which the variable belongs, null if static.
     *
     * @throws NoSuchFieldException if Field does not exist.
     *
     * @return java.lang.reflect.Field
     */
    public static Object getVar(@NotNull Class<?> clazz, String name, Object member) throws NoSuchFieldException, IllegalAccessException {
        Field c = clazz.getDeclaredField(name);
        c.setAccessible(true);
        return c.get(member);
    }
}
