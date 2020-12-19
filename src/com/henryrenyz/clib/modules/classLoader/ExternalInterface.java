package com.henryrenyz.clib.modules.classLoader;

import org.bukkit.plugin.java.JavaPlugin;

public interface ExternalInterface {

    void load(JavaPlugin plugin);

    void unload(JavaPlugin plugin);

}
