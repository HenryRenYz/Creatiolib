package com.henryrenyz.creatiolib.modules;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class hook_CoreProtect {
    public static CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((net.coreprotect.CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }

        return CoreProtect;
    }
}
