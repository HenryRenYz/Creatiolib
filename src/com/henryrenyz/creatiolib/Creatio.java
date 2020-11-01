package com.henryrenyz.creatiolib;

import java.io.IOException;
import java.io.InputStreamReader;

import com.henryrenyz.creatiolib.modules.Messager;
import com.henryrenyz.creatiolib.modules.UpdateScheduler;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import com.henryrenyz.creatiolib.command.CreatioCommandExecutor;
import com.henryrenyz.creatiolib.modules.Config;

import ch.njol.skript.Skript;
import org.bukkit.scheduler.BukkitTask;

public class Creatio extends JavaPlugin {

    public Configuration config;
    public static JavaPlugin plugin;
    public static Boolean hooked_Skript = false;
    public static Boolean hooked_Coreprotect = false;

    private Creatio instance;
    private Boolean enable_update_checker = true;
    private Boolean enable_metrics = true;
    private int configVersion = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("config.yml"))).getInt("version");

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;

        //Config & Language YML
        loadConfig();
        Messager.logStaticConsole("MAIN.LOADING", null);

        //Metrics
        if (enable_metrics) {
            int pluginId = 9236;
            Metrics metrics = new Metrics(plugin, pluginId);
            //metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        }

        //Update Checker
        if (enable_update_checker) {
            BukkitTask task = new UpdateScheduler(plugin).runTaskTimer(plugin, 100, 200);
        }

        //Skript Hook
        if (getServer().getPluginManager().getPlugin("Skript") != null) {
            try {
                hooked_Skript = true;
                Skript.registerAddon(plugin).loadClasses("com.henryrenyz.creatiolib", "skript");
                Messager.logStaticConsole("MAIN.HOOKED", new String[]{"Skript"});
            } catch (IOException e) {
                Messager.logStaticConsole("MAIN.HOOK_FAILED", new String[]{"Skript"});
                e.printStackTrace();
            }
        }

        //Core Protect Hook
        if (getServer().getPluginManager().getPlugin("CoreProtect") != null) {
            hooked_Skript = true;
            Messager.logStaticConsole("MAIN.HOOKED", new String[]{"CoreProtect"});
        }

        //for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        //   playerListName.put(player.getName(), playerData(player));
        //}

        //Command Register
        Bukkit.getPluginCommand("creatio").setExecutor(new CreatioCommandExecutor());

        Messager.logStaticConsole("MAIN.LOADED", null);
    }

    @Override
    public void onDisable() {
    }

    private void loadConfig() {
        Config.updateConfig(plugin, "config.yml", configVersion);
        config = Config.load(plugin, "config.yml");
        enable_update_checker = config.getBoolean("enable_update_checker");
        enable_metrics = config.getBoolean("enable_metrics");

        String lang = config.getString("language");
        Messager.initialization(plugin, lang);
    }
}
