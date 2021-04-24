package vip.creatio.clib.basic.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.channel.Channel;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import vip.creatio.clib.basic.nbt.CompoundTag;
import vip.creatio.common.Pair;
import vip.creatio.accessor.Reflection;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.*;
import org.bukkit.*;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import vip.creatio.common.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public final class BukkitUtil {

    private BukkitUtil() {}


    // Reflection

    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
    public static final String NMS_PKG_NAME = "net.minecraft.server." + VERSION;
    public static final String CB_PKG_NAME = "org.bukkit.craftbukkit." + VERSION;

    public static Class<?> getNmsClass(String name) {
        return ReflectUtil.forName(NMS_PKG_NAME + "." + name);
    }

    public static Class<?> getCbClass(String name) {
        return ReflectUtil.forName(CB_PKG_NAME + '.' + name);
    }



    public static CompoundTag parseNbt(String nbt) {
        try {
            return new CompoundTag(MojangsonParser.parse(nbt));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    // EntityTypes
    private static final HashMap<
            Class<? extends org.bukkit.entity.Entity>,
            Pair<Class<? extends CraftEntity>, Class<? extends Entity>>>
            nmsEntityTypeMap = new HashMap<>();
    private static final HashBiMap<Class<? extends org.bukkit.entity.Entity>, EntityType>
            entityClassTypeMap = HashBiMap.create();
    private static final HashBiMap<
            Class<? extends net.minecraft.server.Entity>,
            EntityTypes<? extends net.minecraft.server.Entity>>
            nmsEntityClassTypeMap = HashBiMap.create();

    static {
        entityMapInit();
    }
    @SuppressWarnings("unchecked")
    // fill all the maps
    private static void entityMapInit() {
        // Get CB Entities
        try {
            String pkgName = CB_PKG_NAME;
            pkgName = pkgName.replace('.', '/');
            Enumeration<URL> e = EntityType.class.getClassLoader().getResources(pkgName);

            for (EntityType t : EntityType.values()) {
                entityClassTypeMap.put(t.getEntityClass(), t);
            }

            for (Field f : EntityTypes.class.getFields()) {
                if (!Modifier.isStatic(f.getModifiers())) continue;
                EntityTypes<?> eValue = (EntityTypes<?>) f.get(null);
                String cls = f.getGenericType().getTypeName();
                cls = cls.substring(cls.indexOf("<") + 1, cls.length() - 1);
                try {
                    Class<?> c = Class.forName(cls);
                    nmsEntityClassTypeMap.put((Class<? extends net.minecraft.server.Entity>) c, eValue);
                } catch (ClassNotFoundException ignored) {}
            }

            while (e.hasMoreElements()) {
                URL next = e.nextElement();
                if (next.getProtocol().equals("jar")) {
                    JarFile jar = ((JarURLConnection) next.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();

                        if (name.charAt(0) == '/')
                            name = name.substring(1);

                        if (name.startsWith(pkgName + "/entity")) {
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                try {

                                    Class<?> cbClass = Class.forName(name = name.substring(0, name.length() - 6).replace('/', '.'));

                                    if (org.bukkit.entity.Entity.class.isAssignableFrom(cbClass)) {

                                        Class<?> nmsClass;

                                        // Getting NMS Class:
                                        //  first try to get through it's name, then it's Constructor parameter if failed.
                                        try {
                                            nmsClass = Class.forName(NMS_PKG_NAME + '.' + "Entity" +
                                                    name.substring(name.lastIndexOf('.') + 5 + 1));
                                        } catch (ClassNotFoundException ignored) {
                                            Constructor<? extends org.bukkit.entity.Entity> c;
                                            c = (Constructor<? extends org.bukkit.entity.Entity>)
                                                    cbClass.getDeclaredConstructors()[0];

                                            if (c.getParameterCount() < 2) continue;

                                            nmsClass = c.getParameterTypes()[1];
                                        }

                                        Class<? extends org.bukkit.entity.Entity> bukkitClass =
                                                (Class<? extends org.bukkit.entity.Entity>) cbClass.getInterfaces()[0];

                                        Pair<Class<? extends CraftEntity>, Class<? extends net.minecraft.server.Entity>> pair =
                                                new Pair<>(
                                                        (Class<? extends CraftEntity>) cbClass,
                                                        (Class<? extends net.minecraft.server.Entity>) nmsClass
                                                );

                                        nmsEntityTypeMap.put(bukkitClass, pair);
                                    }
                                } catch (ClassNotFoundException ignored) {}
                            }
                        }
                    }
                }
            }
        } catch (IOException | IllegalAccessException e) {
            System.err.println("Unable to load Craftbukkit Entity classes from Jar!");
            e.printStackTrace();
        }
    }

    public static Class<? extends net.minecraft.server.Entity>
    toNmsEntityClass(Class<? extends org.bukkit.entity.Entity> bukkitClass) {
        return nmsEntityTypeMap.get(bukkitClass).getValue();
    }

    public static Class<? extends net.minecraft.server.Entity>
    toNmsEntityClass(EntityType type) {
        return nmsEntityTypeMap.get(type.getEntityClass()).getValue();
    }

    public static Class<? extends CraftEntity>
    toCbEntityClass(Class<? extends org.bukkit.entity.Entity> bukkitClass) {
        return nmsEntityTypeMap.get(bukkitClass).getKey();
    }

    public static Class<? extends CraftEntity>
    toCbEntityClass(EntityType type) {
        return nmsEntityTypeMap.get(type.getEntityClass()).getKey();
    }

    public static EntityType
    toBukkitEntityType(Class<? extends org.bukkit.entity.Entity> cls) {
        return entityClassTypeMap.get(cls);
    }

    @SuppressWarnings("unchecked")
    public static <T extends net.minecraft.server.Entity> EntityTypes<T>
    toNmsEntityType(Class<T> cls) {
        return (EntityTypes<T>) nmsEntityClassTypeMap.get(cls);
    }

    public static EntityType
    toBukkitEntityType(EntityTypes<?> types) {
        Class<?> cls = nmsEntityClassTypeMap.inverse().get(types);
        return entityClassTypeMap.get(cls);
    }

    public static EntityTypes<?>
    toNmsEntityType(EntityType type) {
        return toNmsEntityType(toNmsEntityClass(type));
    }



    // Packet

    @SuppressWarnings("unchecked")
    public static <T extends Packet<?>> Class<T> toNmsPacket(Class<vip.creatio.clib.basic.packet.Packet<T>> wrapped) {
        return (Class<T>) vip.creatio.clib.basic.packet.Packet.getNmsClass(wrapped);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Packet<?>> Class<vip.creatio.clib.basic.packet.Packet<T>> toWrappedPacket(Class<T> wrapped) {
        return (Class<vip.creatio.clib.basic.packet.Packet<T>>) vip.creatio.clib.basic.packet.Packet.getWrappedClass(wrapped);
    }


    // Particle
    private static final BiMap<Particle, MinecraftKey> PARTICLES =
            Reflection.<BiMap<Particle, MinecraftKey>>field(CraftParticle.class, "particles").get();
    private static final Map<Particle, Particle> ALIASES =
            Reflection.<Map<Particle, Particle>>field(CraftParticle.class, "aliases").get();

    public static net.minecraft.server.Particle<?> toNms(Particle particle) {
        Particle canonical = particle;
        if (ALIASES.containsKey(particle)) {
            canonical = ALIASES.get(particle);
        }

        return IRegistry.PARTICLE_TYPE.get(PARTICLES.get(canonical));
    }

    public static Particle toBukkit(net.minecraft.server.Particle<?> nms) {
        return CraftParticle.toBukkit(nms);
    }

    public static Particle toBukkit(ParticleParam nms) {
        return CraftParticle.toBukkit(nms);
    }



    // NamespacedKey/MinecraftKey/ResourceLocation

    public static NamespacedKey toBukkit(MinecraftKey nms) {
        return CraftNamespacedKey.fromMinecraft(nms);
    }

    public static MinecraftKey toNms(NamespacedKey key) {
        return CraftNamespacedKey.toMinecraft(key);
    }

    public static NamespacedKey parseKey(@NotNull String key) {
        return CraftNamespacedKey.fromString(key);
    }



    // Difficulty

    private static final BiMap<Difficulty, EnumDifficulty> difficultyMap =
            HashBiMap.create(Arrays.stream(Difficulty.values())
                    .collect(Collectors.toMap(d -> d, d -> EnumDifficulty.valueOf(d.name()))));

    public static EnumDifficulty toNms(Difficulty diff) {
        return difficultyMap.get(diff);
    }

    public static Difficulty toBukkit(EnumDifficulty nms) {
        return difficultyMap.inverse().get(nms);
    }



    // Vector

    public static Vec3D toNms(Vector vec) {
        return CraftVector.toNMS(vec);
    }

    public static Vector toBukkit(Vec3D nms) {
        return CraftVector.toBukkit(nms);
    }



    // World

    public static final WorldServer DEFAULT_WORLD = toNms(Bukkit.getWorlds().get(0));

    public static WorldServer toNms(org.bukkit.World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static CraftWorld toBukkit(WorldServer nms) {
        return nms.getWorld();
    }



    // Sound

    public static SoundEffect toNms(Sound sound) {
        return CraftSound.getSoundEffect(sound);
    }

    public static Sound toBukkit(SoundEffect effect) {
        return CraftSound.getBukkit(effect);
    }



    // Server

    public static DedicatedServer getServer() {
        return toNms(Bukkit.getServer());
    }

    public static DedicatedServer toNms(Server server) {
        return ((CraftServer) server).getServer();
    }

    public static double[] getTps() {
        return getServer().recentTps;
    }
}
