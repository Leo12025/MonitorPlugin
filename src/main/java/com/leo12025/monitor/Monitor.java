package com.leo12025.monitor;


import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

import com.leo12025.monitor.Database;


public final class Monitor extends JavaPlugin implements Listener {

    private PluginLoader loader;
    private Server server;
    private File file;
    private PluginDescriptionFile description;
    private File dataFolder;
    private ClassLoader classLoader;
    private File configFile;
    private PluginLogger logger;
    private FileConfiguration config;


    @Override
    public void onLoad() {
        // Plugin startup logic
        //System.out.println("[Monitor]插件已启动");
        this.logger = new PluginLogger(this);
        logger.log(Level.INFO, "插件已被加载");
        this.loader = getPluginLoader();
        this.server = getServer();
        this.file = getFile();
        this.description = getDescription();
        this.dataFolder = getDataFolder();
        this.classLoader = getClassLoader();
        this.configFile = new File(dataFolder, "config.yml");
        this.logger = new PluginLogger(this);
        this.config = this.getConfig();

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // System.out.println("[Monitor]插件已启动");
        // this.logger = new PluginLogger(this);
        logger.log(Level.INFO, "插件已启动");
        config.addDefault("boardCastType", "feishu");
        config.addDefault("boardCastType", "");
        config.options().copyDefaults(true);
        saveConfig();

        // Enable our class to check for new players using onPlayerJoin()
        this.server.getPluginManager().registerEvents(this, this);
    }

    public void boardCastTo(String message) {
        if (Objects.equals(getConfig().getString("boardCastType"), "feishu")) {
            logger.log(Level.INFO, "向 Feishu 服务推送消息: " + message);

            //TODO: 加入飞书推送相关代码
        }
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        logger.log(Level.INFO,"玩家 " + player.getName() + "加入了游戏，他的 UUID 为: " + player.getUniqueId()+ " 延迟是: "+player.getPing());
        //[16:07:03] [Server thread/ERROR]: [com.leo12025.monitor.Monitor] [Monitor] 玩家 Xyo0加入了游戏，他的 UUID 为:18f72caf-aba0-4e2d-a403-6cec9734338f 延迟是:0
        /*
        if (config.getBoolean("youAreAwesome")) {
            player.sendMessage("You are awesome!");
        } else {
            player.sendMessage("You are not awesome...");
        }*/
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        logger.log(Level.INFO,"玩家 " + player.getName() + "离开了游戏，他的 UUID 为: " + player.getUniqueId());

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
