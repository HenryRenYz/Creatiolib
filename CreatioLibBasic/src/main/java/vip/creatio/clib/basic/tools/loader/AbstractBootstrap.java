package vip.creatio.clib.basic.tools.loader;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * A bootstrap class for plugin that requires custom class loader
 */
public abstract class AbstractBootstrap extends JavaPlugin {

    protected final NmsClassLoader loader;
    protected final PluginInterface delegate;

    protected AbstractBootstrap(NmsClassLoader loader, String mainClass) {
        this.loader = loader;
        init();
        this.delegate = getDelegate(mainClass);
    }

    protected AbstractBootstrap(String mainClass) {
        this.loader = new NmsClassLoader(getClass(), getClassLoader());
        init();
        this.delegate = getDelegate(mainClass);
    }

    private PluginInterface getDelegate(String className) {
        try {
            Class<?> c = loader.getLoadedClasses().get(className);
            @SuppressWarnings("unchecked")
            Constructor<PluginInterface> con = (Constructor<PluginInterface>)
                    c.getDeclaredConstructor(JavaPlugin.class);
            con.setAccessible(true);
            return con.newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Mirror method of ReflectiveClassLoader::getClassIn, can only get classes loaded by NmsClassLoader */
    public final List<Class<?>> getClassIn(String pkg) {
        return loader.getClassIn(pkg);
    }

    /** Mirror method of ReflectiveClassLoader::getClassUnder, can only get classes loaded by NmsClassLoader */
    public final List<Class<?>> getClassUnder(String pkg) {
        return loader.getClassUnder(pkg);
    }

    protected void init() {
        loader.loadClasses();
    }

    @Override
    public void onEnable() {
        delegate.onEnable();
    }

    @Override
    public void onDisable() {
        delegate.onDisable();
        loader.unloadClasses();
        loader.close();
    }

    @Override
    public void onLoad() {
        delegate.onLoad();
    }
}
