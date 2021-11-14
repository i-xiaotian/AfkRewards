package com.tanxi521.afkrewards.main;

import com.tanxi521.afkrewards.commands.AfkCommands;
import com.tanxi521.afkrewards.data.MysqlConnector;
import com.tanxi521.afkrewards.listener.AfkListener;
import com.tanxi521.afkrewards.util.ConfigReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AfkRewards extends JavaPlugin {

    public static AfkRewards instance;

    public static AfkRewards getInstance() {
        return instance;
    }

    public static File file;
    public static FileConfiguration afkData;
    public static File lang;
    public static FileConfiguration langYml;

    @Override
    public void onLoad() {
        getLogger().info("北境挂机奖励即将载入...");
        saveDefaultConfig();
        instance = this;
    }

    @Override
    public void onEnable() {
        if (ConfigReader.getMysqlUsable()) {
            MysqlConnector.getConnection();
        } else if (!ConfigReader.getMysqlUsable()) {
            getLogger().info(ChatColor.YELLOW + "使用本地文件存储");
            file = new File(getDataFolder(), "afkData.yml");
            if (!file.exists()) {
                saveResource(file.getName(), true);
            }
            afkData = YamlConfiguration.loadConfiguration(file);
        }
        lang = new File(getDataFolder(), "message.yml");
        langYml = YamlConfiguration.loadConfiguration(lang);
        if (!lang.exists()) {
            saveResource(lang.getName(), true);
        }
        Bukkit.getPluginCommand("tnafk").setExecutor(new AfkCommands());
        Bukkit.getPluginManager().registerEvents(new AfkListener(), this);
        getLogger().info(ChatColor.GREEN + "插件启动成功");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "北境挂机已卸载");
    }
}
