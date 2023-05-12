package com.sakuno.restaurantmanagesystem.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
@PropertySource("classpath:application.properties")
public class DatabaseEntrance implements DatabaseRepository {

    @Value("${com.sakuno.databaseEntrance.driver}")
    private String databaseDriver;

    @Value("${com.sakuno.databaseEntrance.url}")
    private String databaseUrl;

    @Value("${com.sakuno.databaseEntrance.username}")
    private String databaseUsername;

    @Value("${com.sakuno.databaseEntrance.password}")
    private String databasePassword;

    @Value("${com.sakuno.databaseEntrance.name}")
    private String databaseName;

    private Connection connection;

    private Statement statement = null;

    private boolean initialized = false;

    private boolean available = false;

    @PostConstruct
    public void init() {
        initialized = true;
        try {
            Class.forName(databaseDriver);
            connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        available = true;
    }


    public boolean isAvailable() {
        return available;
    }

    public boolean isInitialized() {
        return initialized;
    }

    private boolean createStatement(PrintStream errorOs) {
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

    public boolean runStatement(String state, PrintStream errorOs) {
        if (!available) {
            errorOs.println("数据库当前不可用！请联系系统管理员！ ErrorCode: 1002");
            return false;
        }

        if (createStatement(errorOs))
            return false;

        try {
            statement.execute(state);
        } catch (Exception ignore) {
            errorOs.println("语句执行失败！请检查数据库语句！" + state + " ErrorCode: 1003");
            return false;
        }

        return true;
    }

    public ResultSet runStatementWithQuery(String state, PrintStream errorOs) {
        if (!available) {
            errorOs.println("数据库当前不可用！请联系系统管理员！ ErrorCode: 1004");
            return null;
        }

        if (createStatement(errorOs))
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
