package com.henryrenyz.creatiolib.packets;

import org.bukkit.Bukkit;

public class MCReflection {

    public static String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);

    public static Class getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    public static Class getCBClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }
}
