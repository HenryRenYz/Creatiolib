package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.Creatio;
import com.henryrenyz.clib.modules.customItem.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItem implements Listener {

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {

        //Player pickup item event
        if (event.getEntity() instanceof Player) {
            //Player Inventory Update
            Bukkit.getScheduler().scheduleSyncDelayedTask(Creatio.getInstance(), () -> ItemManager.updateInv((Player) event.getEntity()), 1L);
        }
    }
}
