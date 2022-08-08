package com.leo12025.monitor;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;

import static com.leo12025.monitor.Monitor.*;
import static com.leo12025.monitor.PlayerData.getPlayerData;
import static com.leo12025.monitor.PlayerData.savePlayerDataForFile;
import static com.leo12025.monitor.PushFeishu.sendFeishuMessage;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerCommandSendEvent(PlayerCommandSendEvent event) {
        //当玩家加入服务器时，我发送给玩家的用于自动填充命令的命令表
        Player player = event.getPlayer();
        Collection<String> commands = event.getCommands();
        //logger.log(Level.INFO, "玩家 "+player.getName()+" 发起了命令 " + commands.toString()+" 的执行");

    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();///msg Xyo0 1
        logger.log(Level.INFO, "玩家 " + player.getName() + " 发起了命令 " + message + " 的执行");
        String[] messageQuery = message.split(" ");
        if (messageQuery.length >= 3 && Objects.equals(messageQuery[0], "/msg")) {
            logger.log(Level.INFO, "玩家 " + player.getName() + " 向 -> " + messageQuery[1] + " : " + messageQuery[2]);
            for (Player p : server.getOnlinePlayers()) {
                if (p.isOp()) {
                    p.sendMessage(ChatColor.GREEN + "[" + player.getName() + "] -> " + messageQuery[1]+ ChatColor.AQUA + " asked: " + messageQuery[2]);
                }
            }
            //Bukkit.broadcast(Component.text("玩家 " + player.getName() + " 向 -> " + messageQuery[1] + " : " + messageQuery[2]), "Monitor.broadcast");
            // [14:23:21 INFO]: Xyo0 lost connection: Internal Exception: java.lang.NoSuchMethodError: 'java.util.Optional net.minecraft.network.protocol.game.ClientboundPlayerChatPacket.d()'
            // 尝试发送消息的时候遇到该问题
        }
        //[21:54:47 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 发起了命令 /msg Xyo0 1 的执行

    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockData blockData = block.getBlockData();
        if(!player.isOp() && !player.hasPermission("monitor.pass") && player.getGameMode() != GameMode.CREATIVE) {
            logger.log(Level.INFO, "玩家 " + player.getName() + " 破坏了方块 " + blockData.getMaterial().translationKey() + "");
            String messageText;
            if ("block.minecraft.enchanting_table".equals(blockData.getMaterial().translationKey())) {
                messageText = "玩家 " + player.getName() + " 破坏了方块 (附魔台) " + blockData.getMaterial().translationKey() + "，位于 x=" + block.getX() + " y=" + block.getY() + " z=" + block.getZ();
                logger.log(Level.INFO, messageText);
                sendFeishuMessage(messageText);
            }
            if ("block.minecraft.beacon".equals(blockData.getMaterial().translationKey())) {
                messageText = "玩家 " + player.getName() + " 破坏了方块 (信标) " + blockData.getMaterial().translationKey() + "，位于 x=" + block.getX() + " y=" + block.getY() + " z=" + block.getZ();
                logger.log(Level.INFO, messageText);
                sendFeishuMessage(messageText);
            }
            String[] MonitorItems = config.getString("Monitor.items").split(",");
            if (Arrays.binarySearch(MonitorItems, blockData.getMaterial().translationKey()) > 0) {
                messageText = "玩家 " + player.getName() + " 破坏了方块 (未知) " + blockData.getMaterial().translationKey() + "，位于 x=" + block.getX() + " y=" + block.getY() + " z=" + block.getZ();
                logger.log(Level.INFO, messageText);
                sendFeishuMessage(messageText);
            }
        }

        //[22:04:42 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 破坏了方块 CraftBlockData{minecraft:grass_block[snowy=false]} 草方块
        //[22:04:39 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 破坏了方块 CraftBlockData{minecraft:grass} 草
        //          blockData.getMaterial().translationKey(); block.minecraft.grass_block
        //          blockData.getMaterial(); GRASS

        //[22:35:53 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 破坏了方块 block.minecraft.grass_block


    }

    public void boardCastToFeishu(String message) {
        if (Objects.equals(config.getString("boardCastType"), "feishu")) {
            logger.log(Level.INFO, "向 Feishu 服务推送消息: " + message);
            sendFeishuMessage(message);

            //TODO: 加入飞书推送相关代码
        }
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        logger.log(Level.INFO, "玩家 " + player.getName() + "加入了游戏，他的 UUID 为: " + player.getUniqueId() + " 延迟是: " + player.getPing() + " IP地址为：" + player.getAddress().getHostString());
        //TODO: 读取信息，如果玩家的Json里面没有regTime，那么就视为第一次加入服务器，那么记录注册时间并保存。
        //[16:07:03] [Server thread/ERROR]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0加入了游戏，他的 UUID 为:18f72caf-aba0-4e2d-a403-6cec9734338f 延迟是:0
        JSONObject obj;

        obj = getPlayerData(player.getUniqueId().toString());
        if (!obj.has("regTime")) {
            logger.log(Level.INFO, "新用户登录服务器，正在记录注册时间。");
            obj.put("regTime", System.currentTimeMillis());
            obj.put("userName", player.getName());
            obj.put("regIp", player.getAddress().getHostString()); // 这里提取到的不是玩家的IP信息！
            try {
                savePlayerDataForFile(player.getUniqueId().toString(), obj.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boardCastToFeishu("玩家 " + player.getName() + "加入了游戏，他的 UUID 为: " + player.getUniqueId() + " IP地址为：" + player.getAddress().getHostString());
            logger.log(Level.INFO, "新用户注册信息记录完毕，推送信息中。");
        }

        if (obj.has("regIp") && !obj.getString("regIp").equals(player.getAddress().getHostString())) {
            boardCastToFeishu("玩家 " + player.getName() + " 通过非注册IP加入了游戏，当前IP为: " + player.getAddress().getHostString());
            logger.log(Level.INFO, "玩家 " + player.getName() + " 通过非注册IP加入了游戏，当前IP为: " + player.getAddress().getHostString());
        }

    }

    @EventHandler
    public void onAsyncChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        TextComponent message = (TextComponent) event.message();
        String receivedMessage = message.content();
        logger.log(Level.INFO, "玩家 " + player.getName() + " 发送了新消息: " + receivedMessage);


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        logger.log(Level.INFO, "玩家 " + player.getName() + "离开了游戏，他的 UUID 为: " + player.getUniqueId());

    }
}
