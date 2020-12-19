package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        //Structure check;
        StructureHandler.onBlockBreakStructureCheck(event);
    }

}
