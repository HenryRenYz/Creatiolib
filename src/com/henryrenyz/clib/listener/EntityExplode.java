package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.MultiblockAPI.StructureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        //Structure check;
        StructureHandler.onEntityExplodeStructureCheck(event);
    }

}
