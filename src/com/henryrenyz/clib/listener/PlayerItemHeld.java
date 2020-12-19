package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.modules.customItem.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemHeld implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {

        //Custom Item Update;
        ItemManager.updateItems(event.getPlayer(), event.getPreviousSlot(), event.getNewSlot());
    }
}
