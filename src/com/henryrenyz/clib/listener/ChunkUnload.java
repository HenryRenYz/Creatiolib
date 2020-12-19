package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnload implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {

        //Structure check;
        StructureHandler.onChunkUnloadCheck(event);
    }

}
