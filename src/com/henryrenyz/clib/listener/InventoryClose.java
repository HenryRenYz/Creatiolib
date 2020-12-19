package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.modules.customItem.ItemManager;
import com.henryrenyz.clib.modules.menu.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        //MenuManager;
        MenuManager.HandlerCloseInv(event);

    }

}
