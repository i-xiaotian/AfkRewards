package com.tanxi521.afkrewards.data;

import com.tanxi521.afkrewards.entity.PlayerAfk;
import com.tanxi521.afkrewards.main.AfkRewards;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalYamlConnector {

    public static void createPlayerData(Player player) {
        List<String> playerList = AfkRewards.afkData.getStringList("playerList");
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        if (!playerList.contains(uuid)) {
            playerList.add(uuid);
            AfkRewards.afkData.set("playerData." + uuid + ".name", name);
            AfkRewards.afkData.set("playerData." + uuid + ".type", "未挂机");
            AfkRewards.afkData.set("playerData." + uuid + ".start", 0);
            AfkRewards.afkData.set("playerData." + uuid + ".end", 0);
            AfkRewards.afkData.set("playerData." + uuid + ".times", 0);
            AfkRewards.afkData.set("playerData." + uuid + ".total", 0);
            AfkRewards.afkData.set("playerList", playerList);
            try {
                AfkRewards.afkData.save(AfkRewards.file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStart(Player player, String type) {
        String uuid = player.getUniqueId().toString();
        AfkRewards.afkData.set("playerData." + uuid + ".type", type);
        AfkRewards.afkData.set("playerData." + uuid + ".start", System.currentTimeMillis());
        try {
            AfkRewards.afkData.save(AfkRewards.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setEnd(Player player, long time) {
        String uuid = player.getUniqueId().toString();
        AfkRewards.afkData.set("playerData." + uuid + ".end", time);
        try {
            AfkRewards.afkData.save(AfkRewards.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PlayerAfk getPlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        PlayerAfk playerAfk = new PlayerAfk();
        playerAfk.setPlayer_uuid(uuid);
        playerAfk.setDisplayName(player.getDisplayName());
        playerAfk.setType(AfkRewards.afkData.getString("playerData." + uuid + ".type"));
        playerAfk.setStart(AfkRewards.afkData.getLong("playerData." + uuid + ".start"));
        playerAfk.setEnd(AfkRewards.afkData.getLong("playerData." + uuid + ".end"));
        playerAfk.setTimes(AfkRewards.afkData.getInt("playerData." + uuid + ".times"));
        playerAfk.setTotal(AfkRewards.afkData.getLong("playerData." + uuid + ".total"));
        return playerAfk;
    }

    public static void resetPlayer(PlayerAfk playerAfk) {
        String uuid = playerAfk.getPlayer_uuid();
        int times = playerAfk.getTimes();
        long total = playerAfk.getTotal();
        AfkRewards.afkData.set("playerData." + uuid + ".type", "未挂机");
        AfkRewards.afkData.set("playerData." + uuid + ".start", 0);
        AfkRewards.afkData.set("playerData." + uuid + ".end", 0);
        AfkRewards.afkData.set("playerData." + uuid + ".times", times);
        AfkRewards.afkData.set("playerData." + uuid + ".total", total);
        try {
            AfkRewards.afkData.save(AfkRewards.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<PlayerAfk> getPlayerList() {
        List<String> playerList = AfkRewards.afkData.getStringList("playerList");
        List<PlayerAfk> playerAfkList = new ArrayList<>();
        String uuid;
        for (String player : playerList) {
            PlayerAfk playerAfk = new PlayerAfk();
            uuid = "playerData." + player;
            playerAfk.setPlayer_uuid(player);
            playerAfk.setDisplayName(AfkRewards.afkData.getString(uuid + ".name"));
            playerAfk.setType(AfkRewards.afkData.getString(uuid + ".type"));
            playerAfk.setStart(AfkRewards.afkData.getLong(uuid + ".start"));
            playerAfk.setEnd(AfkRewards.afkData.getLong(uuid + ".end"));
            playerAfk.setTimes(AfkRewards.afkData.getInt(uuid + ".times"));
            playerAfk.setTotal(AfkRewards.afkData.getLong(uuid + ".total"));
            playerAfkList.add(playerAfk);
        }
        Collections.sort(playerAfkList);
        return playerAfkList;
    }
}
