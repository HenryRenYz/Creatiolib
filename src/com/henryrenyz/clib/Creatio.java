package com.henryrenyz.clib;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import com.henryrenyz.clib.MultiblockAPI.Listener.MultiblockAPIStructureCreate;
import com.henryrenyz.clib.MultiblockAPI.Listener.MultiblockAPIStructureDestroy;
import com.henryrenyz.clib.MultiblockAPI.Listener.MultiblockAPIStructureInteract;
import com.henryrenyz.clib.MultiblockAPI.Listener.MultiblockAPIStructureUnload;
import com.henryrenyz.clib.MultiblockAPI.MachineAPI.BlastFurnace;
import com.henryrenyz.clib.MultiblockAPI.MultiblockStructure;
import com.henryrenyz.clib.MultiblockAPI.StructureManager;
import com.henryrenyz.clib.command.CommandRegister;
import com.henryrenyz.clib.listener.*;
import com.henryrenyz.clib.modules.Message;
import com.henryrenyz.clib.modules.Clock;
import com.henryrenyz.clib.modules.customItem.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.henryrenyz.clib.modules.Config;

import ch.njol.skript.Skript;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.henryrenyz.clib.MultiblockAPI.StructureManager.saveMultiblockStructure;

public final class Creatio extends JavaPlugin {

    //Generic config
    private static Creatio      instance;
    private boolean             hooked_Skript = false;
    private boolean             hooked_Coreprotect = false;

    //MultiblockAPI config
    private boolean             enable_multiblock_api = true;
    private int                 structure_check_radius = 0;
    private int                 max_structure_in_radius = 0;
    private int                 max_structure_in_server = 0;
    private int                 max_active_structure = 0;
    private int                 structure_save_interval = -1;

    private boolean             enable_update_checker = true;
    private boolean             enable_metrics = true;
    private Configuration       config;
    private CommandRegister     plugin_commands;
    private int                 configVersion = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("config.yml"))).getInt("version");

    @Override
    public void onEnable() {
        instance = this;

        //Config & Language YML
        loadConfig();
        loadLanguageConfig();

        Message.sendStatic(Level.INFO, "MAIN.LOADING");

        //Metrics
        if (enable_metrics) {
            int pluginId = 9236;
            Metrics metrics = new Metrics(instance, pluginId);
            //metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        }

        //Update Checker
        if (enable_update_checker) {
            new Updater().run();
        }

        hookThirdParty();

        //Register
        plugin_commands = new CommandRegister("creatiolib");
        plugin_commands.init();
        registerEvent();

        //Function init
        ItemManager.updateAll();

        Message.sendStatic(Level.INFO, "MAIN.LOADED");
    }

    @Override
    public void onDisable() {
        //Serialize MultiblockStructure
        if (enable_multiblock_api) {
            saveMultiblockStructure();
            StructureManager.releaseChunks();
        }
    }

    public void loadAllConfig() {
        loadConfig();
        loadLanguageConfig();
        loadMultiblockStructures();
    }

    //Config setter
    public void loadConfig() {
        Config.updateConfig(instance, "config.yml", configVersion);
        config = Config.load(instance, "config.yml");
        enable_update_checker = config.getBoolean("enable_update_checker");
        enable_metrics = config.getBoolean("enable_metrics");

        //MultiblockAPI config set
        enable_multiblock_api           = config.getBoolean("enable_multiblock_api");

        structure_check_radius          = config.getInt("multiblock_api.structure_check_radius");
        structure_check_radius          = (structure_check_radius < 0) ? 2147483647 : structure_check_radius;

        max_structure_in_radius         = config.getInt("multiblock_api.max_structure_in_radius");
        max_structure_in_radius         = (max_structure_in_radius < 0) ? 2147483647 : max_structure_in_radius;

        max_structure_in_server         = config.getInt("multiblock_api.max_structure_in_server");
        max_structure_in_server         = (max_structure_in_server < 0) ? 2147483647 : max_structure_in_server;

        max_active_structure            = config.getInt("multiblock_api.max_active_structure");
        max_active_structure            = (max_active_structure < 0) ? 2147483647 : max_active_structure;

        structure_save_interval         = config.getInt("multiblock_api.structure_save_interval");
        structure_save_interval         = (structure_save_interval < 0) ? -1 : structure_save_interval;



    }

    public void loadLanguageConfig() {
        String lang = config.getString("language");
        Message.initialization(instance, lang);
    }

    public void loadMultiblockStructures() {
        if (enable_multiblock_api) {
            File cfg = new File(instance.getDataFolder(), "multiblock_structures");
            if (!(cfg.exists()) || (cfg.listFiles().length == 0)) {
                Config.updateConfig(instance, "multiblock_structures/example.yml", -1);
            }
            if (StructureManager.getMultiblockStructures() != null) {
                saveMultiblockStructure();
            }
            int[] load_success = StructureManager.loadAll();
            Message.sendStatic(Level.INFO, "MAIN.RELOAD.DONE", Integer.toString(load_success[0]), Integer.toString(load_success[1]));
        }
    }

    private void hookThirdParty() {
        //Skript Hook
        if (getServer().getPluginManager().getPlugin("Skript") != null) {
            try {
                hooked_Skript = true;
                Skript.registerAddon(instance).loadClasses("com.henryrenyz.creatiolib", "skript");
                Message.sendStatic(Level.INFO, "MAIN.HOOKED", "Skript");
            } catch (IOException e) {
                Message.sendStatic(Level.INFO, "MAIN.HOOKED_FAILED", "Skript");
                e.printStackTrace();
            }
        }

        //Core Protect Hook
        if (getServer().getPluginManager().getPlugin("CoreProtect") != null) {
            hooked_Coreprotect = true;
            Message.sendStatic(Level.INFO, "MAIN.HOOKED", "CoreProtect");
        }
    }

    private void registerEvent() {
        PluginManager manager = getServer().getPluginManager();
        if (enable_multiblock_api) {
            Clock.startScheduler();


            //Register API event listener
            manager.registerEvents(new MultiblockAPIStructureDestroy(), this);
            manager.registerEvents(new MultiblockAPIStructureCreate(), this);
            manager.registerEvents(new MultiblockAPIStructureUnload(), this);
            manager.registerEvents(new MultiblockAPIStructureInteract(), this);


            //Register serialization
            ConfigurationSerialization.registerClass(MultiblockStructure.class);


            //Register Machine
            StructureManager.registerMachine(BlastFurnace.class);


            //load ALL
            loadMultiblockStructures();
        }

        //Event Listener
        manager.registerEvents(new BlockBreak(), this);
        manager.registerEvents(new BlockPlace(), this);
        manager.registerEvents(new PistonExtend(), this);
        manager.registerEvents(new PistonRetract(), this);
        manager.registerEvents(new BlockExplode(), this);
        manager.registerEvents(new EntityExplode(), this);
        manager.registerEvents(new PlayerInteract(), this);
        manager.registerEvents(new ChunkUnload(), this);
        manager.registerEvents(new InventoryOpen(), this);
        manager.registerEvents(new InventoryClose(), this);
        manager.registerEvents(new InventoryClick(), this);
        manager.registerEvents(new InventoryDrag(), this);
        manager.registerEvents(new PlayerItemHeld(), this);
        manager.registerEvents(new EntityPickupItem(), this);
        manager.registerEvents(new PlayerDropItem(), this);
    }

    public static Creatio getInstance() {
        return instance;
    }

    public boolean hookedSkript() {
        return hooked_Skript;
    }

    public boolean hookedCP() {
        return hooked_Coreprotect;
    }

    public boolean enabledMultiblockApi() {
        return enable_multiblock_api;
    }

    public int structureCheckRadius() {
        return structure_check_radius;
    }

    public int maxStructureInRadius() {
        return max_structure_in_radius;
    }

    public int maxStructureInServer() {
        return max_structure_in_server;
    }

    public int maxActiveStructure() {
        return max_active_structure;
    }

    public int structureSaveInterval() {
        return structure_save_interval;
    }

    public boolean enabledUpdateChecker() {
        return enable_update_checker;
    }

    public boolean enabledMetrics() {
        return enable_metrics;
    }

    public int configVersion() {
        return configVersion;
    }
}
