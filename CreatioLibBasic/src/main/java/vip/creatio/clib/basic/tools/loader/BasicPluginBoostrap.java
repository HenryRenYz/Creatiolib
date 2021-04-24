package vip.creatio.clib.basic.tools.loader;

import vip.creatio.clib.basic.annotation.processor.ListenerProcessor;
import vip.creatio.clib.basic.annotation.processor.TaskProcessor;
import vip.creatio.clib.basic.packet.ChannelPacketListener;
import vip.creatio.clib.basic.packet.PacketListener;
import vip.creatio.clib.basic.tools.*;

/**
 * A plugin boostrap class that contains some essential functions that
 * all the plugin needs, like task manager, event listener, annotation
 * preprocessor and custom message system...
 */
public abstract class BasicPluginBoostrap extends AbstractBootstrap {

    /** Task annotation processing service */
    protected GlobalTaskExecutor taskManager;

    /** Listener annotation processing service */
    protected ListenerRegister listenerManager;

    protected BasicPluginBoostrap(NmsClassLoader loader, String mainClass) {
        super(loader, mainClass);
    }

    /**
     * Set package that need to be preload by ReflectiveClassLoader
     * using NmsClassLoader::addIncludePath and NmsClassLoader::addExcludePath
     */
    protected abstract void initNmsLoader();

    @Override
    protected final void init() {
        taskManager = new TaskManager(this);
        listenerManager = new ListenerManager(this);
        loader.addAnnotationProcessor(new ListenerProcessor(listenerManager));
        loader.addAnnotationProcessor(new TaskProcessor(taskManager));
        initNmsLoader();
        super.init();
    }

    public GlobalTaskExecutor getTaskExecutor() {
        return taskManager;
    }

    public ListenerRegister getListenerManager() {
        return listenerManager;
    }

    /** onLoad for inherited class */
    protected void onDelegateLoad() {};

    /** onEnable for inherited class */
    protected void onDelegateEnable() {};

    /** onDisable for inherited class */
    protected void onDelegateDisable() {};

    @Override
    public final void onLoad() {
        super.onLoad();
        onDelegateLoad();
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        onDelegateEnable();
        taskManager.start();
        taskManager.onLoad();
        loader.processAnnotations();
    }

    @Override
    public final void onDisable() {
        taskManager.onUnload();
        onDelegateDisable();
        super.onDisable();
    }
}
