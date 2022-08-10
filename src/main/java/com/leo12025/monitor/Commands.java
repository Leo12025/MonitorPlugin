package com.leo12025.monitor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

import static com.leo12025.monitor.Monitor.logger;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender Sender, @NotNull Command Command, @NotNull String Label, String[] Args) {
        //检查执行指令
        if (Sender instanceof Player Player) {
            //玩家

            //获取到玩家

            Player_Command(Player, Args);
        } else {
            //控制台

            Console_Command(Args);
        }
        return false;
    }

    public void Player_Command(Player player, String[] Args) {
        //检查指令参数
        if (Args.length == 0) {
            //无参数

            //检查当前世界是否可用


        } else if (Args.length == 1) {
            //有参数

            //检查参数内容
            switch (Args[0]) {
                //查看配置文件
                case "info":
                    //历遍可用世界


                    //重载配置文件
                case "reload":
                    //检查玩家是否有插件管理权限
                    if (!player.hasPermission("monitor.reload")) {
                        //不拥有
                        player.sendMessage(ChatColor.GREEN + "您没有使用此命令的权限");


                        return;
                    }

                    break;

                //参数错误
                default:

                    //发送信息给玩家

                    break;
            }
        } else {
            //参数错误

            //发送信息给玩家
            //Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ParameterError")));
        }

    }

    public void Console_Command(String[] Args) {
        //检查指令参数
        if (Args.length == 0) {
            //无参数
            logger.log(Level.INFO, "命令无效，输入 monitor help 来获取帮助");
            //发送信息给控制台
        } else if (Args.length == 1) {
            //有参数

            //检查参数内容
            switch (Args[0]) {
                //查看配置文件
                case "info":
                    break;

                //重载配置文件
                case "reload":
                    break;

                //参数错误
                default:
                    //发送信息给控制台
                    break;
            }
        } else {
            //参数错误

            //发送信息给控制台
        }
    }


}
