package com.sakuno.restaurantmanagesystem.manager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sakuno.restaurantmanagesystem.dataclasses.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantLoginInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantRegisterInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantShowData;
import com.sakuno.restaurantmanagesystem.utils.AuthCode;
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
    private PictureManager pictureManager;

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
            if (headPic != null) firstPutHeadPic(id, headPic, errorOs);
            if (defaultMenu != null) firstSetMenu(id, defaultMenu, errorOs);
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
                        .forColumns("ID", "Name", "Address", "Phone", "Commit")
                        .withCondition("AuthCode", authCode)
                        .build(),
                errorOs
        );

        if (result == null) {
            return null;
        } else try {
            if (result.next()) {
                return new RestaurantFullData(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        authCode
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
                        .forColumns("ID", "Name", "Address", "Phone", "Commit")
                        .withCondition("ID", info.getUsername())
                        .withCondition("ManagePassword", info.getPassword())
                        .build(),
                errorOs
        );


        if (queryResult == null) {
            return null;
        } else try {
            if (queryResult.next()) {
                var authCode = new AuthCode(info.getUsername(), "Restaurant");
                if (repository.runStatement(
                        StateBuilder.Companion
                                .update()
                                .fromTable("Restaurants")
                                .withCondition("ID", info.getUsername())
                                .change("AuthCode", authCode.code)
                                .build(),
                        errorOs
                )) return new RestaurantFullData(
                        queryResult.getString(1),
                        queryResult.getString(2),
                        queryResult.getString(3),
                        queryResult.getString(4),
                        queryResult.getString(5),
                        authCode.code
                );
                else {
                    errorOs.println("登录令牌更新错误！");
                    return null;
                }
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

    public RestaurantShowData getShowData(String restaurantID, PrintStream errorOs) {
        ResultSet resultSet = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .withCondition("ID", restaurantID)
                        .forColumns("Name", "Address", "Phone", "Commit", "Menu")
                        .build(),
                errorOs
        );

        if(resultSet == null) {
            errorOs.println("查询错误！");
            return null;
        } else try {

            if(!resultSet.next()) {
                errorOs.println("目标ID不存在！");
                return null;
            }

            return new RestaurantShowData(
                    restaurantID,
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    new Gson().fromJson(resultSet.getString(5), MenuInfo.class)
            );
        } catch (SQLException ignore) {
            errorOs.println("查询错误！");
            return null;
        } catch (JsonSyntaxException ignore) {
            errorOs.println("菜单不存在！");
            return null;
        }
    }

    private boolean firstPutHeadPic(String id, Part filePart, PrintStream errorOs) {
        String filePath;
        try {
            filePath = pictureManager.saveUploadedPicture(filePart, id, "headPic", null);
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
            if (!pictureManager.removeUploadedFile(filePath, errorOs))
                errorOs.println("文件删除失败！严重错误！");
            return false;
        }
    }

    public boolean setHeadPic(String id, AuthCode authCode, Part filePart, PrintStream errorOs) {
        ResultSet queryResult = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("Restaurants")
                        .forColumns("HeadPic")
                        .withCondition("ID", id)
                        .withCondition("AuthCode", authCode.code)
                        .build(),
                errorOs
        );

        if (queryResult == null) return false;
        else try {
            if (!queryResult.next()) {
                errorOs.println("认证失败！");
                return false;
            }
            String oldFilePath = queryResult.getString(1);
            if (pictureManager.removeUploadedFile(oldFilePath, errorOs)) {
                errorOs.println("原图片删除失败！");
                return false;
            }
            String headPicPath = pictureManager.saveUploadedPicture(filePart, id, "HeadPic", null);

            if (repository.runStatement(
                    StateBuilder.Companion
                            .update()
                            .fromTable("Restaurants")
                            .withCondition("ID", id)
                            .change("HeadPic", headPicPath)
                            .build(),
                    errorOs
            )) return true;

            errorOs.println("数据库更新失败！");
            return false;

        } catch (Exception ignore) {
            errorOs.println("数据库查询错误！");
            return false;
        }
    }

    private boolean firstSetMenu(String restaurantID, MenuInfo info, PrintStream errorOs) {
        return repository.runStatement(
                StateBuilder.Companion
                        .update()
                        .fromTable("Restaurants")
                        .withCondition("ID", restaurantID)
                        .change("Menu", info.getJson())
                        .build(),
                errorOs
        );
    }

    public boolean setMenu(String restaurantID, AuthCode authCode, MenuInfo info, PrintStream errorOs) {
        return repository.runStatement(
                StateBuilder.Companion
                        .update()
                        .fromTable("Restaurants")
                        .withCondition("ID", restaurantID)
                        .withCondition("AuthCode", authCode.code)
                        .change("Menu", info.getJson())
                        .build(),
                errorOs
        );
    }
}