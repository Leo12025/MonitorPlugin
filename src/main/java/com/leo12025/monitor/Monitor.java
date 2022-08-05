package com.leo12025.monitor;


import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public final class Monitor extends JavaPlugin {

    private PluginLoader loader;
    private Server server;
    private File file;
    private PluginDescriptionFile description;
    private File dataFolder;
    private ClassLoader classLoader;
    private File configFile;
    private PluginLogger logger;


    @Override
    public void onLoad() {
        // Plugin startup logic
        //System.out.println("[Monitor]插件已启动");
        this.logger = new PluginLogger(this);
        logger.log(Level.INFO,"插件已启动");
        this.loader = getPluginLoader();
        this.server = getServer();
        this.file = getFile();
        this.description = getDescription();
        this.dataFolder = getDataFolder();
        this.classLoader = getClassLoader();
        this.configFile = new File(dataFolder, "config.yml");
        this.logger = new PluginLogger(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // System.out.println("[Monitor]插件已启动");
        // this.logger = new PluginLogger(this);
        logger.log(Level.INFO,"插件已启动");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // System.out.println("[Monitor]插件已卸载");
        // logger.log(Level.INFO,"插件已卸载");
        saveConfig();
    }
}
