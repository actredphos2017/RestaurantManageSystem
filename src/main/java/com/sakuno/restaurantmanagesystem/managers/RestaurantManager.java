package com.sakuno.restaurantmanagesystem.managers;

import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantRegisterInfo;
import com.sakuno.restaurantmanagesystem.utils.DatabaseEntrance;
import com.sakuno.restaurantmanagesystem.utils.StateBuilder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

public class RestaurantManager {
    private static RestaurantManager singleItem = null;

    private RestaurantManager() {
    }

    public static RestaurantManager getInstance() {
        if (singleItem == null)
            synchronized (CustomerAccountManager.class) {
                singleItem = new RestaurantManager();
            }
        return singleItem;
    }

    private OutputStream failReason = new ByteArrayOutputStream();

    private PrintStream errorOs = new PrintStream(failReason);

    public String popFailReason() {
        var result = failReason.toString();
        errorOs.close();
        failReason = new ByteArrayOutputStream();
        errorOs = new PrintStream(failReason);
        return result;
    }

    public boolean register(RestaurantRegisterInfo info) {
        if (!info.check()) {
            errorOs.println("内容填写不完整！");
            return false;
        }

        var id = info.getId();

        var db = DatabaseEntrance.getInstance();
        if (!db.isAvailable()) {
            errorOs.println("数据库不可用");
            return false;
        }

        var queryResult = db.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .withCondition("ID", id)
                        .build()
        );

        if (queryResult == null) {
            errorOs.println("查询错误");
            errorOs.println(db.popFailReason());
            return false;
        }

        try {
            if (queryResult.next()) {
                errorOs.println("相同ID的餐厅已存在");
                return false;
            }
        } catch (SQLException e) {
            errorOs.println("数据库错误");
        }

        var prepareState = StateBuilder.Companion
                .insert()
                .into("Restaurants")
                .forColumns("ID", "ManagePassword", "Name", "Address", "Phone", "HeadPic", "Commit")
                .withItems(
                        info.getId(),
                        info.getManagePassword(),
                        info.getName(),
                        info.getAddress(),
                        info.getPhone(),
                        info.getHeadPic(),
                        info.getCommit())
                .build();

        if (!db.runStatement(
                prepareState
        )) {
            errorOs.println("存在输入超限的参数！" + prepareState);
            return false;
        } else return true;
    }

    public RestaurantFullData checkAuthCode(String authCode) {
        var db = DatabaseEntrance.getInstance();
        if (!db.isAvailable()) {
            errorOs.println("数据库不可用");
            return null;
        }

        var result = db.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .forColumns("ID", "Name", "Address", "Phone", "HeadPic", "Commit")
                        .withCondition("AuthCode", authCode)
                        .build()
        );

        if (result == null) {
            errorOs.println(db.popFailReason());
            return null;
        } else try {
            if (result.next()) {
                return new RestaurantFullData(
                        result.getString(3),
                        result.getString(6),
                        result.getString(5),
                        result.getString(1),
                        result.getString(2),
                        result.getString(4)
                );
            } else {
                errorOs.println("登录已过期");
                return null;
            }
        } catch (Exception ignore) {
            errorOs.println("查询错误");
            return null;
        }
    }
}