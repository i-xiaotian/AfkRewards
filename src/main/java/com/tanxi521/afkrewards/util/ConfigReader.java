package com.tanxi521.afkrewards.util;

import com.tanxi521.afkrewards.main.AfkRewards;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

/**
 * 读取config.yml配置文件工具类
 */
public class ConfigReader {


    //获取奖励时间间隔
    public static int getRewardsTime() {
        return getPlugin().getConfig().getInt("rewardsTime");
    }

    //获取最大允许的挂机人数
    public static int getMaxAfkPlayer() {
        return getPlugin().getConfig().getInt("maxAfkPlayer");
    }

    //获取是否使用数据库
    public static boolean getMysqlUsable() {
        return getPlugin().getConfig().getBoolean("mysql");
    }

    //获取数据库链接
    public static String getMysqlLocalHost() {
        String ip = getPlugin().getConfig().getString("ip");
        String port = getPlugin().getConfig().getString("port");
        String database = getPlugin().getConfig().getString("database");
        return "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    }

    //获取数据库用户
    public static String getUser() {
        return getPlugin().getConfig().getString("user");
    }

    //获取数据库密码
    public static String getPassword() {
        return getPlugin().getConfig().getString("password");
    }

    //获取所有配置过的挂机类型
    public static List<String> getTypes() {
        return getPlugin().getConfig().getStringList("types");
    }

    //获取挂机奖励的执行指令
    public static List<String> getCommand(CommandSender sender , String type) {
        if (sender.hasPermission("onlinemoney.vip")) {
            return getPlugin().getConfig().getStringList("rewardsCommands." + type + ".vip");
        } else {
            return getPlugin().getConfig().getStringList("rewardsCommands" + type + ".common");
        }
    }

    //获取插件实例
    public static AfkRewards getPlugin() {
        return AfkRewards.getInstance();
    }

    //禁用数据库使用本地存储
    public static void disableMysql() {
        getPlugin().getConfig().set("mysql",false);
        getPlugin().reloadConfig();
    }
}
