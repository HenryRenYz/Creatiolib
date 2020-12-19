package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonRetract implements Listener {

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {

        //Structure check;
        StructureHandler.onPistonRetractStructureCheck(event);
    }

}
