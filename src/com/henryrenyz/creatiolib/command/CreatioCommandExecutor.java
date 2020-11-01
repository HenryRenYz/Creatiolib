package com.henryrenyz.creatiolib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CreatioCommandExecutor implements CommandExecutor {

    public CreatioCommandExecutor() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("creatio.admin")) {
            sender.sendMessage("It Did you don't have perm creatio.admin");
            return false;
        }
        return false;
    }
}

