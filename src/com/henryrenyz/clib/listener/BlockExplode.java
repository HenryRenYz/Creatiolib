package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplode implements Listener {

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {

        //Structure check;
        StructureHandler.onBlockExplodeStructureCheck(event);
    }

}
