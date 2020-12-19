package com.henryrenyz.clib.modules;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import com.henryrenyz.clib.MultiblockAPI.StructureManager;
import com.henryrenyz.clib.modules.customItem.ItemManager;
import com.henryrenyz.clib.modules.menu.MenuManager;
import org.bukkit.scheduler.BukkitScheduler;

import static com.henryrenyz.clib.Creatio.*;

public final class Clock {

    private static Double tick = 0d;

    public static void startScheduler() {
        BukkitScheduler scheduler = getInstance().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(getInstance(), () -> {
            tick++;

            //1 tick loop
            StructureHandler.StructureTick1S();
            MenuManager.TickMenu();

            //2 tick loop
            if (tick % 2 == 0) {




            }
            //0.5 second loop
            if (tick % 10 == 0) {





            }
            //1 second loop
            if (tick % 20 == 0) {





            }
            //3 second loop
            if (tick % 60 == 0) {
                //Tick Structure
                StructureHandler.StructureTick3S();

                //Tick Player Inventory
                ItemManager.updateAll();
            }
            //Structure Save Interval
            if (getInstance().structureSaveInterval() > 40 && tick % getInstance().structureSaveInterval() == 0) {
                StructureManager.saveMultiblockStructure();
            }
        }, 20L, 1L);
    }
}
