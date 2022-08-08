package com.leo12025.monitor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        //检查执行指令
        if (Sender instanceof Player) {
            //玩家

            //获取到玩家
            Player Player = (Player) Sender;

            Player_Command(Player, Args);
        } else {
            //控制台

            Console_Command(Args);
        }
        return false;
    }

    public boolean Player_Command(Player Player, String[] Args) {
        //检查指令参数
        if (Args.length == 0) {
            //无参数

            //检查当前世界是否可用

                return false;




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
                    if (!Player.hasPermission("newrandomtp.cooldown")) {
                        //不拥有


                        return false;
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

        return false;
    }

    public void Console_Command(String[] Args) {
        //检查指令参数
        if (Args.length == 0) {
            //无参数

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
