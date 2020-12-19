package com.henryrenyz.clib.modules.customItem;

import org.bukkit.entity.Player;

import java.util.List;

public interface DynamicLore {

    void update(Player player);

    List<String> toList();

}
