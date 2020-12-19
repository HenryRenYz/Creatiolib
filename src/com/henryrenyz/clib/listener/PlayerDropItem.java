package com.henryrenyz.clib.listener;

import com.henryrenyz.clib.Creatio;
import com.henryrenyz.clib.modules.customItem.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        //Player Inventory Update
        Bukkit.getScheduler().scheduleSyncDelayedTask(Creatio.getInstance(), () -> ItemManager.updateInv(event.getPlayer()), 1L);
    }
}
