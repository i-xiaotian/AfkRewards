package com.tanxi521.afkrewards.entity;

import com.tanxi521.afkrewards.data.AfkData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class AfkMap {
    /**
     * 创建挂机玩家的保存地址
     */
    public static final Map<String, PlayerAfk> afkPlayer = new HashMap<>();

    /**
     * 将正在挂机的玩家添加进list
     */
    public static void addAfkPlayer(PlayerAfk playerAfk) {
        String realName = playerAfk.getDisplayName();
        if (!afkPlayer.containsKey(realName)) {
            afkPlayer.put(realName, playerAfk);
        }
    }

    /**
     * 玩家退出挂机状态时
     * 将玩家移除限制列表
     */
    public static void removeAfkPlayer(String playerName) {
        afkPlayer.remove(playerName);
    }

    /**
     * 检查玩家是否在挂机
     */
    public static boolean hasPlayerIn(String playerName) {
        return afkPlayer.containsKey(playerName);
    }
}
