package com.henryrenyz.clib.modules.packet;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;
import com.henryrenyz.clib.modules.reflection.ReflectionField;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;
import com.henryrenyz.clib.modules.reflection.ReflectionUtils;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Packet {

    private Object packet = null;

    Packet() {}

    Packet(Object packet) {
        this.packet = packet;
    }

    //Send packet
    public void send(Player... p) {

        for (Player player : p) {
            sendPacket(player);
        }
    }

    //Send packet to all players in world(s)
    public void distribute(World world) {
        for (Player p : world.getPlayers()) {
            sendPacket(p);
        }
    }

    //Send packet to all players in current server
    public void broadcast() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendPacket(p);
        }
    }

    private void sendPacket(Player p) {
        try {
            ReflectionMethod.PlayerConnection_sendPacket.m
                    .invoke(ReflectionField.EntityPlayer_playerConnection.f.get(ReflectionUtils.getNMSPlayer(p)), this.packet);
        } catch (InvocationTargetException | IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    //Packet Play In
    public static class in {

        in() {}

    }

    //Packet Play Out
    public static class out {

        out() {}

        //PlayServerWorldParticles
        public static Packet WorldParticles(Location loc, String Particle, float ofx, float ofy, float ofz, float ex, int num, boolean isF, Object... Data) {
            try {
                //Packet Setup
                Object param;
                Object par = ReflectionClass.Particles.c.getField(Particle).get(null);
                if (Particle.equalsIgnoreCase("DUST")) {
                    param = ReflectionClass.ParticleParamRedstone.c.getConstructor(float.class, float.class, float.class, float.class).newInstance(Data[0], Data[1], Data[2], Data[3]);
                } else if (Particle.equalsIgnoreCase("ITEM")) {
                    param = ReflectionClass.ParticleParamItem.c.getConstructor(ReflectionClass.Particle.c, ReflectionClass.ItemStack.c)
                            .newInstance(ReflectionClass.Particle.c.cast(par), ReflectionUtils.getNMSItemStack((ItemStack) Data[0]));
                } else if (Particle.equalsIgnoreCase("BLOCK") || Particle.equalsIgnoreCase("FALLING_DUST")) {
                    param = ReflectionClass.ParticleParamBlock.c.getConstructor(ReflectionClass.Particle.c, ReflectionClass.IBlockData.c)
                            .newInstance(ReflectionClass.Particle.c.cast(par), ReflectionUtils.getNMSBlockData((BlockData) Data[0]));
                } else {
                    param = ReflectionClass.ParticleType.c.cast(par);
                }

                return new Packet(ReflectionClass.Packet.c.cast(ReflectionClass.PacketPlayOutWorldParticles.c
                        .getConstructor(ReflectionClass.ParticleParam.c, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class)
                        .newInstance(param, isF, loc.getX(), loc.getY(), loc.getZ(), ofx, ofy, ofz, ex, num)));
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | NullPointerException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        //PlayOutSetCooldown
        public static Packet SetCooldown(Material material, int ticks) {
            try {
                return new Packet(ReflectionClass.Packet.c.cast(ReflectionClass.PacketPlayOutSetCooldown.c.getConstructor(ReflectionClass.Item.c, int.class)
                        .newInstance(ReflectionUtils.getNMSItem(material), ticks)));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        //PlayOutServerDifficulty    #Fake difficulty
        @SuppressWarnings("deprecation")
        public static Packet ServerDifficulty(Difficulty difficulty, boolean isLocked) {
            try {
            /*   A Replacement in case Bukkit's are unusable(Actually not:])
            Integer diffvalue = 0;
            switch (difficulty.name()) {
                case "PEACEFUL": diffvalue = 0; break;
                case "EASY": diffvalue = 1; break;
                case "NORMAL": diffvalue = 2; break;
                case "HARD": diffvalue = 3; break;
            }
             */
                Object Difficulty = ReflectionClass.EnumDifficulty.c.getMethod("getById",int.class).invoke(null, difficulty.getValue());
                return new Packet(ReflectionClass.Packet.c.cast(ReflectionClass.PacketPlayOutServerDifficulty.c.getConstructor(ReflectionClass.EnumDifficulty.c, boolean.class)
                        .newInstance(Difficulty, isLocked)));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        //PlayOutGameStateChange
        public static Packet GameStateChange(EnumGameState state, Float value) {
            try {
                return new Packet(ReflectionClass.Packet.c.cast(ReflectionClass.PacketPlayOutGameStateChange.c.getConstructor(ReflectionClass.PacketPlayOutGameStateChange$a.c,float.class)
                        .newInstance(ReflectionClass.PacketPlayOutGameStateChange$a.c.getConstructor(int.class).newInstance(state.getValue()),value)));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        //PlayOutGameStateChange
        public static Packet Chat(String message, @Nullable EnumChatMessageType type, @Nullable UUID uuid) {
            try {
                Object component = ReflectionClass.IChatBaseComponent$ChatSerializer.c.getMethod("a", String.class).invoke(null, message);
                return new Packet(ReflectionClass.Packet.c.cast(ReflectionClass.PacketPlayOutChat.c.getConstructor(ReflectionClass.IChatBaseComponent.c, ReflectionClass.ChatMessageType.c,UUID.class)
                        .newInstance(component,type.getNMS(),uuid)));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
