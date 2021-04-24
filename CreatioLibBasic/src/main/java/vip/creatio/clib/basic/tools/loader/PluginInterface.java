package vip.creatio.clib.basic.tools.loader;

public interface PluginInterface {

    default void onEnable() {}

    default void onDisable() {}

    default void onLoad() {}

}
