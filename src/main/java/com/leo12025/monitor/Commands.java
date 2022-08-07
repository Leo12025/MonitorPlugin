package com.leo12025.monitor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.equals("basic")) {
            // 你要写的代码
            return true;
        }

        if(sender instanceof Player){
            Player p = (Player)sender;
            p.sendMessage(ChatColor.WHITE+"毫无作用的指令");
        }
        else{
            System.out.println("该命令只能由玩家使用");
        }


        return false;
    }
}
