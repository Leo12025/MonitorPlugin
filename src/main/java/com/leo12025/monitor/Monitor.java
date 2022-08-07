package com.leo12025.monitor;


import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;

import static com.leo12025.monitor.PlayerData.getPlayerData;
import static com.leo12025.monitor.PlayerData.savePlayerDataForFile;
import static com.leo12025.monitor.PushFeishu.sendFeishuMessage;


public final class Monitor extends JavaPlugin implements Listener {

    public static File dataFolder;
    public static PluginLogger logger;
    private PluginLoader loader;
    private Server server;
    private File file;
    private PluginDescriptionFile description;
    private ClassLoader classLoader;
    private File configFile;
    public static FileConfiguration config;


    @Override
    public void onLoad() {
        // Plugin startup logic
        //System.out.println("[Monitor]插件已启动");
        logger = new PluginLogger(this);
        logger.log(Level.INFO, "插件已被加载");
        this.loader = getPluginLoader();
        this.server = getServer();
        this.file = getFile();
        this.description = getDescription();
        dataFolder = getDataFolder();
        this.classLoader = getClassLoader();
        this.configFile = new File(dataFolder, "config.yml");
        logger = new PluginLogger(this);
        config = this.getConfig();
        sendFeishuMessage("Player Xyo0 break block.minecraft.enchanting_table, In x=-132 y=78 z=97");
        sendFeishuMessage("玩家 Xyo0 破坏了方块 (附魔台) block.minecraft.enchanting_table，位于 x=-132 y=78 z=97");


    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // System.out.println("[Monitor]插件已启动");
        // this.logger = new PluginLogger(this);
        logger.log(Level.INFO, "插件已启动");
        config.addDefault("boardCastType", "feishu");
        config.addDefault("Feishu.app_id", "请填写app_id");
        config.addDefault("Feishu.app_secret", "请填写app_secret");
        config.addDefault("Feishu.sendGroup", "请填写需要推送到的群");
        config.addDefault("Monitor.items", "需要监测的方块信息，以;分割，需要是完整的方块注册名");
        config.options().copyDefaults(true);

        saveConfig();

        // Enable our class to check for new players using onPlayerJoin()
        this.server.getPluginManager().registerEvents(this, this);
    }


    @EventHandler
    public void onPlayerCommandSendEvent(PlayerCommandSendEvent event) {
        //当玩家加入服务器时，我发送给玩家的用于自动填充命令的命令表
        Player player = event.getPlayer();
        Collection commands = event.getCommands();
        //logger.log(Level.INFO, "玩家 "+player.getName()+" 发起了命令 " + commands.toString()+" 的执行");

    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();///msg Xyo0 1
        logger.log(Level.INFO, "玩家 " + player.getName() + " 发起了命令 " + message + " 的执行");
        //[21:54:47 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 发起了命令 /msg Xyo0 1 的执行

    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockData blockData = block.getBlockData();

        logger.log(Level.INFO, "玩家 " + player.getName() + " 破坏了方块 " + blockData.getMaterial().translationKey() + "");
        String messageText;
        switch (blockData.getMaterial().translationKey().toString()){
            case "block.minecraft.enchanting_table":{
                messageText="玩家 " + player.getName() + " 破坏了方块 (附魔台) " + blockData.getMaterial().translationKey() + "，位于 x="+block.getX()+" y="+block.getY()+" z="+block.getZ();
                logger.log(Level.INFO, messageText);
                sendFeishuMessage(messageText);
                break;
            }
            default:break;

        }
        //[22:04:42 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 破坏了方块 CraftBlockData{minecraft:grass_block[snowy=false]} 草方块
        //[22:04:39 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 破坏了方块 CraftBlockData{minecraft:grass} 草
        //          blockData.getMaterial().translationKey(); block.minecraft.grass_block
        //          blockData.getMaterial(); GRASS

        //[22:35:53 INFO]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0 破坏了方块 block.minecraft.grass_block


    }

    public void boardCastToFeishu(String message) {
        if (Objects.equals(getConfig().getString("boardCastType"), "feishu")) {
            logger.log(Level.INFO, "向 Feishu 服务推送消息: " + message);
            sendFeishuMessage(message);

            //TODO: 加入飞书推送相关代码
        }
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        logger.log(Level.INFO, "玩家 " + player.getName() + "加入了游戏，他的 UUID 为: " + player.getUniqueId() + " 延迟是: " + player.getPing());
        //TODO: 读取信息，如果玩家的Json里面没有regTime，那么就视为第一次加入服务器，那么记录注册时间并保存。
        //[16:07:03] [Server thread/ERROR]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0加入了游戏，他的 UUID 为:18f72caf-aba0-4e2d-a403-6cec9734338f 延迟是:0
        JSONObject obj;

        obj = getPlayerData(player.getUniqueId().toString());
        if (!obj.has("regTime")) {
            logger.log(Level.INFO, "新用户登录服务器，正在记录注册时间。");
            obj.put("regTime", System.currentTimeMillis());
            try {
                savePlayerDataForFile(player.getUniqueId().toString(), obj.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boardCastToFeishu("玩家 " + player.getName() + "加入了游戏，他的 UUID 为: " + player.getUniqueId());
            logger.log(Level.INFO, "新用户注册时间记录完毕，推送信息中。");
        }
        if (!obj.has("userName")) {

            logger.log(Level.INFO, "新用户登录服务器，正在记录注册userName。");
            obj.put("userName", player.getName());
            try {
                savePlayerDataForFile(player.getUniqueId().toString(), obj.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.log(Level.INFO, "新用户userName记录完毕，推送信息中。");
            boardCastToFeishu("2玩家 " + player.getName() + "加入了游戏，他的 UUID 为: " + player.getUniqueId());

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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // System.out.println("[Monitor]插件已卸载");
        logger.log(Level.INFO, "插件已卸载");
        // 通知保存插件配置
        saveConfig();
    }
}
