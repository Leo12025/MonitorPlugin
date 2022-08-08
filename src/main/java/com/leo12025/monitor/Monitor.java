package com.leo12025.monitor;


import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public final class Monitor extends JavaPlugin {

    public static File dataFolder;
    public static PluginLogger logger;
    public static FileConfiguration config;
    private PluginLoader loader;
    public static  Server server;
    private File file;
    private PluginDescriptionFile description;
    private ClassLoader classLoader;
    private File configFile;

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
        //sendFeishuMessage("测试服务器推送\n换行");
        //sendFeishuMessage("玩家 Xyo0 破坏了方块 (附魔台) block.minecraft.enchanting_table，位于 x=-132 y=78 z=97");


    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // System.out.println("[Monitor]插件已启动");
        // this.logger = new PluginLogger(this);
        logger.log(Level.INFO, "插件已启动");

        LoadConfig();
        // Enable our class to check for new players using onPlayerJoin()
        //getCommand("monitor").setExecutor(new Commands());

        // LoadCommand();
        this.server.getPluginManager().registerEvents(new PlayerListener(), this);
    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // System.out.println("[Monitor]插件已卸载");
        logger.log(Level.INFO, "插件已卸载");
        // 通知保存插件配置
        saveConfig();
    }
    public void LoadConfig(){
        config.addDefault("boardCastType", "feishu");
        config.addDefault("Feishu.app_id", "请填写app_id");
        config.addDefault("Feishu.app_secret", "请填写app_secret");
        config.addDefault("Feishu.sendGroup", "请填写需要推送到的群");
        config.addDefault("Feishu.webHook", "");
        config.addDefault("Monitor.items", "需要监测的方块信息，以,分割，需要是完整的方块注册名");
        config.addDefault("Monitor.serverName", "服务器1");
        config.options().copyDefaults(true);
        saveConfig();

    }
    public void LoadCommand() {
        getCommand("tpr").setExecutor(new Commands());
    }
}
