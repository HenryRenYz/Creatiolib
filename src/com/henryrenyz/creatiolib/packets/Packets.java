package com.henryrenyz.creatiolib.packets;

import com.sun.istack.internal.NotNull;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class Packets{

    static Class packet;
    static Class particleParam;
    static Class particleType;
    static Class particle;
    static Class particles;
    static Class item;
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
            item = MCReflection.getNMSClass("Item");
            itemStack = MCReflection.getNMSClass("ItemStack");
            iBlockData = MCReflection.getNMSClass("IBlockData");

            craftItemStack = MCReflection.getCBClass("inventory.CraftItemStack");
            craftBlockData = MCReflection.getCBClass("block.data.CraftBlockData");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Packet Sender
    public static void sendPacket(@NotNull Player[] p, Object pk) {
        for (Player player : p) {
            try {
                Object entityPlayer = getNMSPlayer(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

                playerConnection.getClass().getMethod("sendPacket", packet).invoke(playerConnection, pk);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    //Bukkit Player -> NMS Entity Player
    private static Object getNMSPlayer(@NotNull Player p) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object craftPlayer = MCReflection.getCBClass("entity.CraftPlayer").cast(p);
        return craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
    }

    //Bukkit Material -> NMS Item
    private static Object getNMSItem(@NotNull Material item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object itemstack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, new ItemStack(item));
        return itemStack.getMethod("getItem").invoke(itemstack);
    }

    //Bukkit ItemStack -> NMS ItemStack
    private static Object getNMSItemStack(@NotNull ItemStack item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
    }

    //Bukkit BlockData -> NMS BlockData
    private static Object getNMSBlockData(@NotNull BlockData data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return craftBlockData.cast(data).getClass().getMethod("getState").invoke(craftBlockData.cast(data));
    }

    //Packet Register
    //PlayServerWorldParticles
    public static Object PlayOutWorldParticles(Location loc, String Particle, float ofx, float ofy, float ofz, float ex, int num, boolean isF, Object[] Data) {
        try {
            //Packet Setup
            Object param;
            Object par = particles.getField(Particle).get(null);
            if (Particle.equalsIgnoreCase("DUST")) {
                Class particleParamRedstone = MCReflection.getNMSClass("ParticleParamRedstone");
                param = particleParamRedstone.getConstructor(float.class, float.class, float.class, float.class).newInstance(Data[0], Data[1], Data[2], Data[3]);
            } else if (Particle.equalsIgnoreCase("ITEM")) {
                Class particleParamItem = MCReflection.getNMSClass("ParticleParamItem");
                param = particleParamItem.getConstructor(particle, MCReflection.getNMSClass("ItemStack"))
                        .newInstance(particle.cast(par), getNMSItemStack((ItemStack) Data[0]));
            } else if (Particle.equalsIgnoreCase("BLOCK") || Particle.equalsIgnoreCase("FALLING_DUST")) {
                Class particleParamBlock = MCReflection.getNMSClass("ParticleParamBlock");
                param = particleParamBlock.getConstructor(particle, iBlockData)
                        .newInstance(particle.cast(par), getNMSBlockData((BlockData) Data[0]));
            } else {
                param = particleType.cast(par);
            }

            return packet.cast(MCReflection.getNMSClass("PacketPlayOutWorldParticles")
                    .getConstructor(particleParam, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class)
                    .newInstance(param, isF, loc.getX(), loc.getY(), loc.getZ(), ofx, ofy, ofz, ex, num));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException | NullPointerException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    //PlayOutSetCooldown
    public static Object PlayOutSetCooldown(Material material, int ticks) {
        try {
            return packet.cast(MCReflection.getNMSClass("PacketPlayOutSetCooldown").getConstructor(item, int.class)
            .newInstance(getNMSItem(material), ticks));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    //PlayOutServerDifficulty    #Fake difficulty
    public static Object PlayOutServerDifficulty(Difficulty difficulty, boolean isLocked) {
        try {
            Class diff = MCReflection.getNMSClass("EnumDifficulty");
            /*   A Replacement in case Bukkit's were unusable(Actually not:])
            Integer diffvalue = 0;
            switch (difficulty.name()) {
                case "PEACEFUL": diffvalue = 0; break;
                case "EASY": diffvalue = 1; break;
                case "NORMAL": diffvalue = 2; break;
                case "HARD": diffvalue = 3; break;
            }
             */
            Object Difficulty = diff.getMethod("getById",int.class).invoke(null, difficulty.getValue());
            return packet.cast(MCReflection.getNMSClass("PacketPlayOutServerDifficulty").getConstructor(diff, boolean.class)
                    .newInstance(Difficulty, isLocked));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
