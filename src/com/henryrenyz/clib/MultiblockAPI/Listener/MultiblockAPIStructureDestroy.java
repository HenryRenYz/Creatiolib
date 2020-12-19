package com.henryrenyz.clib.MultiblockAPI.Listener;

import com.henryrenyz.clib.MultiblockAPI.EventRegister.MultiblockAPIStructureDestoryEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MultiblockAPIStructureDestroy implements Listener {

    @EventHandler
    public void onStructureDestroy(MultiblockAPIStructureDestoryEvent event) {

        //Do sth.
    }
}
