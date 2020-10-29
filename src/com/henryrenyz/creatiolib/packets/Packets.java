package com.henryrenyz.creatiolib.packets;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class Packets{

    static Class packet;
    static Class particleParam;
    static Class particleType;
    static Class particle;
    static Class particles;
    static Class itemStack;
    static Class iBlockData;

    static Class craftItemStack;
    static Class craftBlockData;

    static {
        try {
            packet = MCReflection.getNMSClass("Packet");
            particle = MCReflection.getNMSClass("Particle");
            particles = MCReflection.getNMSClass("Particles");
            particleParam = MCReflection.getNMSClass("ParticleParam");
            particleType = MCReflection.getNMSClass("ParticleType");
            itemStack = MCReflection.getNMSClass("ItemStack");
            iBlockData = MCReflection.getNMSClass("IBlockData");

            craftItemStack = MCReflection.getCBClass("inventory.CraftItemStack");
            craftBlockData = MCReflection.getCBClass("block.data.CraftBlockData");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Packet Register
    //PlayServerWorldParticles
    public static Object PlayServerWorldParticles(Location loc, String Particle, float ofx, float ofy, float ofz, float ex, int num, boolean isF, Object[] Data) {
        try {
            //Packet Setup
            Object param;
            Object par = particles.getField(Particle).get(null);
            if (Particle.equalsIgnoreCase("DUST")) {
                Class particleParamRedstone = MCReflection.getNMSClass("ParticleParamRedstone");
                param = particleParamRedstone.getConstructor(float.class, float.class, float.class, float.class).newInstance(Data[0], Data[1], Data[2], Data[3]);
            } else if (Particle.equalsIgnoreCase("ITEM")) {
                Class particleParamItem = MCReflection.getNMSClass("ParticleParamItem");
                Object Item = (ItemStack) Data[0];
                param = particleParamItem.getConstructor(particle, MCReflection.getNMSClass("ItemStack"))
                        .newInstance(particle.cast(par), craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, Item));
            } else if (Particle.equalsIgnoreCase("BLOCK") || Particle.equalsIgnoreCase("FALLING_DUST")) {
                Class particleParamBlock = MCReflection.getNMSClass("ParticleParamBlock");
                Object Block = craftBlockData.cast(Data[0]);
                param = particleParamBlock.getConstructor(particle, iBlockData)
                        .newInstance(particle.cast(par), Block.getClass().getMethod("getState").invoke(Block));
            } else {
                param = particleType.cast(par);
            }

            return MCReflection.getNMSClass("Packet").cast(MCReflection.getNMSClass("PacketPlayOutWorldParticles")
                    .getConstructor(particleParam, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class)
                    .newInstance(param, isF, loc.getX(), loc.getY(), loc.getZ(), ofx, ofy, ofz, ex, num));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException | NullPointerException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void sendPacket(Player[] p,Object pk) {
        for (Player player : p) {
            try {
                Object craftPlayer = MCReflection.getCBClass("entity.CraftPlayer").cast(player);
                Object entityPlayer = craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

                playerConnection.getClass().getMethod("sendPacket", packet).invoke(playerConnection, pk);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
