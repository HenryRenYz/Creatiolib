package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.modules.menu.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDrag implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {

        //MenuManager;
        MenuManager.HandlerDragInv(event);
    }

}
