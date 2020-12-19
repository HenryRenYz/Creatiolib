package com.henryrenyz.clib.MultiblockAPI.Listener;

import com.henryrenyz.clib.MultiblockAPI.EventRegister.MultiblockAPIStructureUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MultiblockAPIStructureUnload implements Listener {

    @EventHandler
    public void onStructureUnload(MultiblockAPIStructureUnloadEvent event) {
        System.out.println("Yes, this is unloaded!!!!!!!");
    }
}
