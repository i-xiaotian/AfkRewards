package com.tanxi521.afkrewards.util;
import java.sql.*;

/**
 * JDBC连接工具类
 */
public class JDBCUtils {
    static String url;
    static String user;
    static String password;
    static {
        url = ConfigReader.getMysqlLocalHost();
        user = ConfigReader.getUser();
        password = ConfigReader.getPassword();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws Exception {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
    public static void close(Statement statement , Connection connection ) {
        try {
            if( statement != null ) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if( connection != null ) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(ResultSet resultSet ,Statement statement , Connection connection ) {
        try {
            if( resultSet != null ) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(statement,connection);

    }
}
