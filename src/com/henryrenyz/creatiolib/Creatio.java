package com.henryrenyz.creatiolib;

import java.io.IOException;

import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import com.henryrenyz.creatiolib.command.CreatioCommandExecutor;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public final class Creatio extends JavaPlugin {

    Creatio instance;
    SkriptAddon addon;

    private ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);

        //Metrics
        int pluginId = 9236;
        Metrics metrics = new Metrics(this, pluginId);
        //metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));

        //Skript Register
        try {
            addon.loadClasses("com.henryrenyz.creatiolib", "skript");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Logger
        getLogger().info("§6§lCreatio Lib §e§lEnabled!");

        //for (Player player : Bukkit.getServer().getOnlinePlayers()) {

        //   playerListName.put(player.getName(), playerData(player));
        //}

        //Command Register
        Bukkit.getPluginCommand("creatio").setExecutor(new CreatioCommandExecutor());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
