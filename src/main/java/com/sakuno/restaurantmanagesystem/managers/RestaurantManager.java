package com.sakuno.restaurantmanagesystem.managers;

import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantLoginInfo;
import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantRegisterInfo;
import com.sakuno.restaurantmanagesystem.utils.DatabaseRepository;
import com.sakuno.restaurantmanagesystem.utils.StateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.sql.SQLException;

@Service
public class RestaurantManager {

    @Autowired
    private DatabaseRepository repository;


    public boolean register(RestaurantRegisterInfo info, PrintStream errorOs) {
        if (!info.check()) {
            errorOs.println("内容填写不完整！");
            return false;
        }

        var id = info.getId();

        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用");
            return false;
        }

        var queryResult = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .withCondition("ID", id)
                        .build(),
                errorOs
        );

        if (queryResult == null) {
            errorOs.println("查询错误");
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

        if (!repository.runStatement(prepareState, errorOs)) {
            errorOs.println("存在输入超限的参数！" + prepareState);
            return false;
        } else return true;
    }

    public RestaurantFullData checkAuthCode(String authCode, PrintStream errorOs) {

        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用");
            return null;
        }

        var result = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .forColumns("ID", "Name", "Address", "Phone", "HeadPic", "Commit")
                        .withCondition("AuthCode", authCode)
                        .build(),
                errorOs
        );

        if (result == null) {
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

    public String getHeadPic(String id, PrintStream errorOs) {

        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用");
            return null;
        }

        var queryResult = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .forColumns("HeadPic")
                        .withCondition("ID", id)
                        .build(),
                errorOs
        );

        if (queryResult == null) {
            return null;
        } else try {
            if (queryResult.next()) {
                return queryResult.getString(1);
            } else {
                errorOs.println("目标 ID 不存在");
                return null;
            }
        } catch (SQLException ignore) {
            errorOs.println("查询失败！");
            return null;
        }
    }

    public RestaurantFullData login(RestaurantLoginInfo info, PrintStream errorOs) {
        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用");
            return null;
        }

        var queryResult = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .forColumns("ID", "Name", "Address", "Phone", "HeadPic", "Commit")
                        .withCondition("ID", info.getUsername())
                        .withCondition("ManagePassword", info.getPassword())
                        .build(),
                errorOs
        );

        RestaurantFullData res;

        if (queryResult == null) {
            return null;
        } else try {
            if (queryResult.next()) {
                res = new RestaurantFullData(
                        queryResult.getString(3),
                        queryResult.getString(6),
                        queryResult.getString(5),
                        queryResult.getString(1),
                        queryResult.getString(2),
                        queryResult.getString(4)
                );
                return res;
            } else {
                errorOs.println("账号不存在或密码错误！");
                return null;
            }
        } catch (SQLException ignore) {
            errorOs.println("查询失败！");
            return null;
        }

    }
}