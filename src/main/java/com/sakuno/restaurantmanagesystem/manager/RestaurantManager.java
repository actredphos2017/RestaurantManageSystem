package com.sakuno.restaurantmanagesystem.manager;

import com.google.gson.Gson;
import com.sakuno.restaurantmanagesystem.dataclasses.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantLoginInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantRegisterInfo;
import com.sakuno.restaurantmanagesystem.utils.DatabaseRepository;
import com.sakuno.restaurantmanagesystem.utils.StateBuilder;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Service
public class RestaurantManager {

    @Autowired
    private DatabaseRepository repository;

    @Autowired
    private FileManager fileManager;

    public RestaurantFullData register(RestaurantRegisterInfo info, Part headPic, MenuInfo defaultMenu, PrintStream errorOs) throws IOException {
        if (!info.check()) {
            errorOs.println("内容填写不完整！");
            return null;
        }

        var id = info.getId();

        var queryResult = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .withCondition("ID", id)
                        .build(),
                errorOs
        );

        if (queryResult == null)
            return null;

        try {
            if (queryResult.next()) {
                errorOs.println("相同ID的餐厅已存在");
                return null;
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
                        Objects.requireNonNullElse(info.getHeadPic(), ""),
                        info.getCommit())
                .build();

        if (!repository.runStatement(prepareState, errorOs)) {
            errorOs.println("存在输入超限的参数！" + prepareState);
            return null;
        } else {
            if (headPic != null) putHeadPic(id, headPic, errorOs);
            if (defaultMenu != null) setMenu(id, defaultMenu, errorOs);
            return login(new RestaurantLoginInfo(info.getManagePassword(), id), errorOs);
        }
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

    public Boolean existID(String id, PrintStream errorOs) {
        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用");
            return null;
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
            return null;
        }

        try {
            return queryResult.next();
        } catch (SQLException e) {
            errorOs.println("数据库错误");
            return null;
        }
    }

    public boolean putHeadPic(String id, Part filePart, PrintStream errorOs) {
        String filePath;
        try {
            filePath = fileManager.saveUploadedFile(filePart, id, "headPic");
        } catch (IOException ignore) {
            errorOs.println("上传失败");
            return false;
        }

        try {
            if (!repository.isAvailable())
                throw new Exception();
            if (!repository.runStatement(
                    StateBuilder.Companion
                            .update()
                            .fromTable("Restaurants")
                            .withCondition("ID", id)
                            .change("HeadPic", filePath)
                            .build(),
                    errorOs
            )) throw new Exception();
            return true;
        } catch (Exception ignore) {
            errorOs.println("数据库更新错误！");
            if (!fileManager.removeUploadedFile(filePath, errorOs))
                errorOs.println("文件删除失败！严重错误！");
            return false;
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

    public MenuInfo getMenu(String restaurantID, PrintStream errorOs) {
        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用！");
            return null;
        }

        ResultSet resultSet = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .withCondition("ID", restaurantID)
                        .forColumns("Menu")
                        .build(),
                errorOs
        );

        if (resultSet == null) {
            errorOs.println("查询失败！");
            return null;
        } else try {
            if (!resultSet.next()) {
                errorOs.println("目标餐厅不存在！");
                return null;
            } else {
                return new Gson().fromJson(resultSet.getString(1), MenuInfo.class);
            }
        } catch (Exception ignore) {
            errorOs.println("查询失败！");
            return null;
        }
    }

    public boolean setMenu(String restaurantID, MenuInfo info, PrintStream errorOs) {
        return repository.runStatement(
                StateBuilder.Companion
                        .update()
                        .fromTable("Restaurants")
                        .withCondition("ID", restaurantID)
                        .change("Menu", new Gson().toJson(info))
                        .build(),
                errorOs
        );
    }
}