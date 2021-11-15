package com.tanxi521.afkrewards.util;

import com.tanxi521.afkrewards.data.AfkData;
import com.tanxi521.afkrewards.entity.AfkMap;
import com.tanxi521.afkrewards.entity.PlayerAfk;
import com.tanxi521.afkrewards.main.AfkRewards;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础功能工具类
 */
public class BaseUtils {

    //换算时间
    public static String time(int time) {
        int h = time / 3600;
        int m = (time - h * 3600) / 60;
        int s = (time - h * 3600) % 60;
        String times = h + "时" + m + "分" + s + "秒";
        return times;
    }

    //返回挂机持续时间字符串
    public static String afkTime(PlayerAfk playerAfk) {
        long start = playerAfk.getStart();
        long now = System.currentTimeMillis();
        int time = (int) (now - start) / 1000;
        return time(time);
    }

    //返回挂机总时间字符串
    public static String totalAfkTime(PlayerAfk playerAfk) {
        long start = playerAfk.getStart();
        long end = playerAfk.getEnd();
        int time = (int) (end - start) / 1000;
        return time(time);
    }

    //计算挂机结束后的奖励次数
    public static int rewardsTime(PlayerAfk playerAfk) {
        long start = playerAfk.getStart();
        long end = playerAfk.getEnd();
        int time = (int) (end - start);
        int rewardsTime = ConfigReader.getRewardsTime();
        return time / 1000 / 60 / rewardsTime;
    }

    public static void giveRewards(PlayerAfk playerAfk, CommandSender sender) {
        int rewardsTime = rewardsTime(playerAfk);
        if (rewardsTime == 0) {
            sender.sendMessage("§e[提示]本次挂机没有达到获得奖励时间哦");
            return;
        }
        List<String> commands = ConfigReader.getCommand(sender, playerAfk.getType());
        while (rewardsTime > 0) {
            for (String cmd : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", sender.getName()));
            }
            rewardsTime--;
        }
    }

    public static void endPlayerAfk(PlayerAfk playerAfk, int times) {
        long total = playerAfk.getEnd() - playerAfk.getStart();
        playerAfk.setTimes(playerAfk.getTimes() + times);
        playerAfk.setTotal(playerAfk.getTotal() + total);
        AfkData.resetPlayer(playerAfk);
        AfkMap.removeAfkPlayer(playerAfk.getDisplayName());
    }

    public static String getLang(String key) {
        return AfkRewards.langYml.getString(key);
    }

    public static List<String> getLangList(String key) {
        return AfkRewards.langYml.getStringList(key);
    }

    public static List<String> rankList() {
        List<PlayerAfk> playerList = AfkData.getPlayerList();
        List<String> rankList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i >= playerList.size()) {
                break;
            }
            PlayerAfk playerAfk = playerList.get(i);
            String name = playerAfk.getDisplayName();
            String afkTime = time((int) playerAfk.getTotal() / 1000);
            String afkTimes = playerAfk.getTimes() + "";
            rankList.add("§eNo." + (i + 1) + " 玩家: §r" + name + " §e,挂机总时间 §r" + afkTime + " §e获得奖励: §r" + afkTimes + "§e次");
        }
        return rankList;
    }

}
