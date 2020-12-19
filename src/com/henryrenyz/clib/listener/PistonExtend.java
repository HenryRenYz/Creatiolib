package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonExtend implements Listener {

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {

        //Structure check;
        StructureHandler.onPistonExtendStructureCheck(event);
    }

}
