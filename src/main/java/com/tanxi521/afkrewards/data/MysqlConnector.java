package com.tanxi521.afkrewards.data;

import com.tanxi521.afkrewards.entity.PlayerAfk;
import com.tanxi521.afkrewards.main.AfkRewards;
import com.tanxi521.afkrewards.util.ConfigReader;
import com.tanxi521.afkrewards.util.JDBCUtils;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MysqlConnector {
    //尝试连接数据库
    public static void getConnection() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "create table if not exists afkrewards(player_uuid varchar(255) primary key not null ," +
                    "displayName varchar(255) not null,type varchar(255) not null , start bigint ,end bigint , times int , total bigint);";
            statement.execute(sql);
            AfkRewards.getInstance().getLogger().info("§e数据库连接成功");
        } catch (Exception e) {
            AfkRewards.getInstance().getLogger().warning("§c数据库出错，将使用本地存储");
            ConfigReader.disableMysql();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }

    //创建玩家数据
    public static void createPlayerData(Player player) {
        String player_uuid = player.getUniqueId().toString();
        String name = player.getName();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into afkrewards(player_uuid,displayName,type,start,end,times,total)" +
                    " select ?,?,?,0,0,0,0 from dual where not exists(select displayName from afkrewards where displayName=?);";
            statement = connection.prepareStatement(sql);
            statement.setString(1, player_uuid);
            statement.setString(2, name);
            statement.setString(3, "未挂机");
            statement.setString(4, name);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }

    //设置玩家开始挂机时间
    public static void setStart(Player player, String type) {
        String uuid = player.getUniqueId().toString();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update afkrewards set type = ? ,start = ? where player_uuid=?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, type);
            statement.setLong(2, System.currentTimeMillis());
            statement.setString(3, uuid);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }

    //设置玩家结束挂机时间
    public static void setEnd(Player player, long time) {
        String uuid = player.getUniqueId().toString();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update afkrewards set end = ? where player_uuid=?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, time);
            statement.setString(2, uuid);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }

    /*
    设置玩家结束挂机状态
    public static void resetType(Player player) {
        String player_uuid = player.getUniqueId().toString();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update afkrewards set type = ? where uuid = ?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1,"未挂机");
            statement.setString(2, uuid);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }*/

    /*
    设置玩家挂机奖励次数
    public static void setTimes(Player player , int times) {
        String player_uuid = player.getUniqueId().toString();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update afkrewards set times = ? where uuid = ?;";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,times);
            statement.setString(2, uuid);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }*/

    //获取玩家挂机数据
    public static PlayerAfk getPlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        PlayerAfk playerAfk = new PlayerAfk();
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from afkrewards where player_uuid=?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            resultSet = statement.executeQuery();
            resultSet.first();
            playerAfk.setPlayer_uuid(resultSet.getString("player_uuid"));
            playerAfk.setDisplayName(resultSet.getString("displayName"));
            playerAfk.setType(resultSet.getString("type"));
            playerAfk.setStart(resultSet.getLong("start"));
            playerAfk.setEnd(resultSet.getLong("end"));
            playerAfk.setTimes(resultSet.getInt("times"));
            playerAfk.setTotal(resultSet.getLong("total"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, statement, connection);
        }
        return playerAfk;
    }

    //玩家结束挂机时恢复玩家数据
    public static void resetPlayer(PlayerAfk playerAfk) {
        String uuid = playerAfk.getPlayer_uuid();
        int times = playerAfk.getTimes();
        long total = playerAfk.getTotal();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "update afkrewards set type = ? , start = ? ,end = ? , times = ? , total = ? where player_uuid=?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "未挂机");
            statement.setLong(2, 0);
            statement.setLong(3, 0);
            statement.setInt(4, times);
            statement.setLong(5, total);
            statement.setString(6, uuid);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, statement, connection);
        }
    }

    public static List<PlayerAfk> getPlayerList() {
        List<PlayerAfk> playerAfkList = new ArrayList<>();
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from afkrewards;";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PlayerAfk playerAfk = new PlayerAfk();
                playerAfk.setPlayer_uuid(resultSet.getString("player_uuid"));
                playerAfk.setDisplayName(resultSet.getString("displayName"));
                playerAfk.setType(resultSet.getString("type"));
                playerAfk.setStart(resultSet.getLong("start"));
                playerAfk.setEnd(resultSet.getLong("end"));
                playerAfk.setTimes(resultSet.getInt("times"));
                playerAfk.setTotal(resultSet.getLong("total"));
                playerAfkList.add(playerAfk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, statement, connection);
        }
        Collections.sort(playerAfkList);
        return playerAfkList;
    }
}
