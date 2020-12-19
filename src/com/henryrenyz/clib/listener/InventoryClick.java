package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.modules.customItem.ItemManager;
import com.henryrenyz.clib.modules.menu.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        //MenuManager;
        MenuManager.HandlerClickInv(event);

        //Custom Item Update
        ItemManager.onInventoryClick(event);

    }

}
