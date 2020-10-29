package com.henryrenyz.creatiolib.command;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class CreatioCommandExecutor implements CommandExecutor {

    public CreatioCommandExecutor() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("creatio.admin")) {
            sender.sendMessage("It Did you don't have perm creatio.admin");
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage("长度不对233");
            return false;

        } else {
            CoreProtectAPI CoreProtect = ((net.coreprotect.CoreProtect) getServer().getPluginManager().getPlugin("CoreProtect")).getAPI();
            Player target = Bukkit.getPlayerExact(args[0]);
            Material target_block = Material.getMaterial(args[1]);
            if (target == null) {
                sender.sendMessage("§c玩家不在线");
                return true;
            } else if (target_block == null) {
                sender.sendMessage("§c必须是一个材料名称");
                return true;
            } else {
                Location loc = target.getTargetBlockExact(10, FluidCollisionMode.SOURCE_ONLY).getLocation();
                if (loc == null) {
                    loc = target.getLocation();
                }
                //CoreProtect.logChat(target, "This is just a test lol");
                if (CoreProtect != null) {
                    boolean success = CoreProtect.logPlacement(args[0], loc, target_block, null);
                    if (success) {
                        sender.sendMessage("成功添加CoreProtect放置记录");
                    } else {
                        sender.sendMessage("§c添加CoreProtect放置记录失败");
                    }
                } else {
                    sender.sendMessage("§cCoreProtect未安装，For the reason Why! API版本： " + CoreProtect.APIVersion());
                }
                return true;
            }
        }
    }
}

