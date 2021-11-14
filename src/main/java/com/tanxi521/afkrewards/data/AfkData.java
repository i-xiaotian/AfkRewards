package com.tanxi521.afkrewards.data;

import com.tanxi521.afkrewards.entity.PlayerAfk;
import com.tanxi521.afkrewards.util.ConfigReader;
import org.bukkit.entity.Player;

import java.util.List;

public class AfkData {

    static boolean mysql = ConfigReader.getMysqlUsable();

    public static void createPlayerData(Player player) {
        if (mysql) {
            MysqlConnector.createPlayerData(player);
        } else {
            LocalYamlConnector.createPlayerData(player);
        }
    }

    public static void setStart(Player player, String type) {
        if (mysql) {
            MysqlConnector.setStart(player, type);
        } else {
            LocalYamlConnector.setStart(player, type);
        }
    }

    public static void setEnd(Player player, long time) {
        if (mysql) {
            MysqlConnector.setEnd(player, time);
        } else {
            LocalYamlConnector.setEnd(player, time);
        }
    }

    public static PlayerAfk getPlayerData(Player player) {
        if (mysql) {
            return MysqlConnector.getPlayerData(player);
        } else {
            return LocalYamlConnector.getPlayerData(player);
        }
    }

    public static void resetPlayer(PlayerAfk playerAfk) {
        if (mysql) {
            MysqlConnector.resetPlayer(playerAfk);
        } else {
            LocalYamlConnector.resetPlayer(playerAfk);
        }
    }

    public static List<PlayerAfk> getPlayerList() {
        if (mysql) {
            return MysqlConnector.getPlayerList();
        } else {
            return LocalYamlConnector.getPlayerList();
        }
    }
}
