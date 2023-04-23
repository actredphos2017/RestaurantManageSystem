package com.sakuno.restaurantmanagesystem.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;

public class DatabaseEntrance {

    private static DatabaseEntrance singleItem = null;

    public static DatabaseEntrance getInstance() {
        if (singleItem == null)
            synchronized (DatabaseEntrance.class) {
                singleItem = new DatabaseEntrance();
            }
        return singleItem;
    }

    public static boolean init(String driver, String url, String username, String password, String databaseName) {
        var it = getInstance();
        it.initialized = true;

        try {
            Class.forName(driver);
            it.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace(it.errorOs);
            return false;
        }
        it.databaseName = databaseName;
        it.available = true;
        return true;
    }

    private Connection connection;
    private Statement statement = null;

    private boolean initialized = false;
    private boolean available = false;

    private OutputStream failReason = new ByteArrayOutputStream();

    private PrintStream errorOs = new PrintStream(failReason);

    public String popFailReason() {
        var result = "数据库:" + failReason.toString();
        errorOs.close();
        failReason = new ByteArrayOutputStream();
        errorOs = new PrintStream(failReason);
        return result;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isInitialized() {
        return initialized;
    }

    private String databaseName;

    private DatabaseEntrance() {
    }

    private boolean createStatement() {
        try {
            statement = connection.createStatement();
            if (statement == null)
                throw new Exception("Statement Create Failed!");
            try {
                statement.execute("use " + databaseName);
            } catch (Exception ignore) {
                throw new Exception("Cannot Find Database: " + databaseName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorOs.println("创建会话失败！请联系系统管理员！ ErrorCode: 1001");
            return true;
        }
        return false;
    }

    public boolean runStatement(String state) {
        if (!available) {
            errorOs.println("数据库当前不可用！请联系系统管理员！ ErrorCode: 1002");
            return false;
        }

        if (createStatement())
            return false;

        try {
            statement.execute(state);
        } catch (Exception ignore) {
            errorOs.println("语句执行失败！请检查数据库语句！" + state + " ErrorCode: 1003");
            return false;
        }

        return true;
    }

    public ResultSet runStatementWithQuery(String state) {
        if (!available) {
            errorOs.println("数据库当前不可用！请联系系统管理员！ ErrorCode: 1004");
            return null;
        }

        if (createStatement())
            return null;

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(state);
        } catch (Exception ignore) {
            errorOs.println("语句执行失败！请检查数据库语句！" + state + " ErrorCode: 1005");
        }

        return resultSet;
    }
}
