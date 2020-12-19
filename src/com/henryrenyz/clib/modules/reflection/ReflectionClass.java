package com.henryrenyz.clib.modules.reflection;

import com.henryrenyz.clib.modules.Message;
import org.bukkit.Bukkit;

/**
 * An enum of reflection class, basically reflected from
 * net.minecraft.server package and craftbukkit package.
 *
 * Get the class of an enum: etc. XXX.c
 *
 * @variable c the Class represented by enum.
 */

public enum ReflectionClass {

    //Everything about Packet
    Packet                                          (RegisterNMS("Packet")),
    PacketPlayOutChat                               (RegisterNMS("PacketPlayOutChat")),
    PacketPlayOutSetCooldown                        (RegisterNMS("PacketPlayOutSetCooldown")),
    PacketPlayOutWorldParticles                     (RegisterNMS("PacketPlayOutWorldParticles")),
    PacketPlayOutGameStateChange                    (RegisterNMS("PacketPlayOutGameStateChange")),
    PacketPlayOutServerDifficulty                   (RegisterNMS("PacketPlayOutServerDifficulty")),
    PacketPlayOutGameStateChange$a                  (RegisterNMS("PacketPlayOutGameStateChange$a")),

    //Everything about Particle
    Particle                                        (RegisterNMS("Particle")),
    Particles                                       (RegisterNMS("Particles")),
    ParticleType                                    (RegisterNMS("ParticleType")),
    ParticleParam                                   (RegisterNMS("ParticleParam")),
    ParticleParamItem                               (RegisterNMS("ParticleParamItem")),
    ParticleParamBlock                              (RegisterNMS("ParticleParamBlock")),
    ParticleParamRedstone                           (RegisterNMS("ParticleParamRedstone")),

    //Everything about message
    ChatMessageType                                 (RegisterNMS("ChatMessageType")),
    IChatBaseComponent                              (RegisterNMS("IChatBaseComponent")),
    IChatBaseComponent$ChatSerializer               (RegisterNMS("IChatBaseComponent$ChatSerializer")),

    //Everything about Item and Block
    Item                                            (RegisterNMS("Item")),
    ItemStack                                       (RegisterNMS("ItemStack")),
    IBlockData                                      (RegisterNMS("IBlockData")),

    //Everythign about Entity
    EntityPlayer                                    (RegisterNMS("EntityPlayer")),

    //Everything about NBT
    NBTBase                                         (RegisterNMS("NBTBase")),
    NBTTagEnd                                       (RegisterNMS("NBTTagEnd")),
    NBTTagInt                                       (RegisterNMS("NBTTagInt")),
    NBTTagList                                      (RegisterNMS("NBTTagList")),
    NBTTagByte                                      (RegisterNMS("NBTTagByte")),
    NBTTagLong                                      (RegisterNMS("NBTTagLong")),
    NBTTagFloat                                     (RegisterNMS("NBTTagFloat")),
    NBTTagShort                                     (RegisterNMS("NBTTagShort")),
    NBTTagDouble                                    (RegisterNMS("NBTTagDouble")),
    NBTTagString                                    (RegisterNMS("NBTTagString")),
    NBTTagIntArray                                  (RegisterNMS("NBTTagIntArray")),
    NBTTagCompound                                  (RegisterNMS("NBTTagCompound")),
    NBTTagByteArray                                 (RegisterNMS("NBTTagByteArray")),
    NBTTagLongArray                                 (RegisterNMS("NBTTagLongArray")),
    MojangsonParser                                 (RegisterNMS("MojangsonParser")),

    //All enums in NMS
    EnumDifficulty                                  (RegisterNMS("EnumDifficulty")),

    //Networks
    PlayerConnection                                (RegisterNMS("PlayerConnection")),

    //Others
    IRegistry                                       (RegisterNMS("IRegistry")),
    MinecraftKey                                    (RegisterNMS("MinecraftKey")),
    RegistryMaterials                               (RegisterNMS("RegistryMaterials")),

    //Craftbukkit class
    // System
    CraftServer                                     (RegisterCB("CraftServer")),
    // Entity
    CraftPlayer                                     (RegisterCB("entity.CraftPlayer")),
    // Inventory
    CraftMetaItem                                   (RegisterCB("inventory.CraftMetaItem")),
    CraftItemStack                                  (RegisterCB("inventory.CraftItemStack")),
    //  Block
    CraftBlockData                                  (RegisterCB("block.data.CraftBlockData")),
    ;
    public final Class<?> c;
    ReflectionClass(Class<?> clazz) {
        this.c = clazz;
    }

    private static Class<?> RegisterNMS(String name) {
        try {
            return ReflectionUtils.getNMSClass(name);
        } catch (ClassNotFoundException e) {
            Message.debug("&4Registration of NMS class from string &6&l" + name + "&4 failed!");
            e.printStackTrace();
            return null;
        }
    }

    private static Class<?> RegisterCB(String name) {
        try {
            return ReflectionUtils.getCBClass(name);
        } catch (ClassNotFoundException e) {
            Message.debug("&4Registration of CraftBukkit class from string &6&l" + name + "&4 failed!");
            e.printStackTrace();
            return null;
        }
    }
}
