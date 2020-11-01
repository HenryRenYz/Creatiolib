package com.henryrenyz.creatiolib.modules;

import com.henryrenyz.creatiolib.Creatio;
import com.henryrenyz.creatiolib.Updater;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateScheduler extends BukkitRunnable {

    private JavaPlugin plugin;

    public UpdateScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //https://github.com/HenryRenYz/CreatioLib/releases
        new Updater(Creatio.plugin, 85307).getVersion(version -> {
            if (!Creatio.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                Messager.logStaticConsole("MAIN.UPDATE", null);
            }
        });
        this.cancel();
    }
}
