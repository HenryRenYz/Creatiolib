package vip.creatio.clib.basic.tools.loader;

import vip.creatio.accessor.ReflectiveClassLoader;
import vip.creatio.accessor.annotation.AnnotationProcessor;
import vip.creatio.accessor.annotation.DelegationProcessor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPluginLoader;
import vip.creatio.common.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class NmsClassLoader extends ReflectiveClassLoader {

    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
    protected static final byte[] NMS_REPLACE_FROM = "net/minecraft/server".getBytes();
    protected static final byte[] CB_REPLACE_FROM = "org/bukkit/craftbukkit".getBytes();
    protected static final byte[] NMS_REPLACE_TO = ("net/minecraft/server/" + VERSION).getBytes();
    protected static final byte[] CB_REPLACE_TO = ("org/bukkit/craftbukkit/" + VERSION).getBytes();

    private static final Class<?> PLUGIN_CLS_LOADER = ReflectUtil.forName("org.bukkit.plugin.java.PluginClassLoader");
    private static final Field PCL_CLASSES = ReflectUtil.field(PLUGIN_CLS_LOADER, "classes");
    private static final Field PCL_LOADER = ReflectUtil.field(PLUGIN_CLS_LOADER, "loader");
    private static final Field JPL_CLASSES = ReflectUtil.field(JavaPluginLoader.class, "classes");


    protected final Map<String, Class<?>> classes;                    //PluginClassLoader
    protected final Map<String, Class<?>> globalClasses;             //JavaPluginLoader

    public NmsClassLoader(URL jarPath, ClassLoader parent) {
        super(jarPath, parent);
        this.classes = ReflectUtil.get(PCL_CLASSES, parent);
        this.globalClasses = ReflectUtil.get(JPL_CLASSES, ReflectUtil.get(PCL_LOADER, parent));

        super.shouldLoad = s -> classes.get(s) == null;

        addBytecodeReplacement(NMS_REPLACE_FROM, NMS_REPLACE_TO);
        addBytecodeReplacement(CB_REPLACE_FROM, CB_REPLACE_TO);

        addProcessor(new DelegationProcessor());
    }

    public NmsClassLoader(Class<?> classInJar, ClassLoader parent) {
        this(classInJar.getProtectionDomain().getCodeSource().getLocation(), parent);
    }

    public NmsClassLoader(Class<?> classInJar) {
        this(classInJar, classInJar.getClassLoader());
    }

    public void addAnnotationProcessor(AnnotationProcessor<?> processor) {
        addProcessor(processor);
    }

    /** public version of addIncludePackage */
    public void addIncludePath(String pkgPath) {
        addIncludePackage(pkgPath);
    }

    /** public version of addExcludePath */
    public void addExcludePath(String pkgPath) {
        addExcludePackage(pkgPath);
    }

    /** public version of addGlobalPath */
    public void addGlobalPath(String pkgPath) {
        addGlobalPackage(pkgPath);
    }

    public void loadClasses() {
        // Process class bytecode and then load
        ProtectionDomain domain = getClass().getProtectionDomain();
        super.loadClasses(getParent(), domain);
        classes.putAll(getLoadedClasses());
    }

    public JarFile getJar() {
        return jar;
    }

    public void unloadClasses() {
        for (String s : new ArrayList<>(classes.keySet())) {
            classes.remove(s);
            globalClasses.remove(s);
        }
    }

    public Map<String, Class<?>> getClasses() {
        return new HashMap<>(classes);
    }

    public Map<String, Class<?>> getBukkitGlobalClasses() {
        return new HashMap<>(globalClasses);
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
