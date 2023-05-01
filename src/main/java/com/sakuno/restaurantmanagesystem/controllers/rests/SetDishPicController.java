package com.sakuno.restaurantmanagesystem.controllers.rests;

import com.sakuno.restaurantmanagesystem.dataclasses.menu.Dish;
import com.sakuno.restaurantmanagesystem.dataclasses.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.rests.TaskResponse;
import com.sakuno.restaurantmanagesystem.manager.MenuRequestHolder;
import com.sakuno.restaurantmanagesystem.manager.PictureManager;
import com.sakuno.restaurantmanagesystem.manager.RestaurantManager;
import com.sakuno.restaurantmanagesystem.utils.AuthCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

@RestController
public class SetDishPicController {

    @Autowired
    PictureManager pictureManager;

    @Autowired
    RestaurantManager restaurantManager;

    @Autowired
    MenuRequestHolder holder;

    @PostMapping("/rest/upload_dish_pic")
    public String onPost(MultipartHttpServletRequest request) throws ServletException, IOException {
        Part filePart = request.getPart("picture");
        String targetDish = request.getHeader("target_dish");
        String restaurantID = request.getHeader("id");
        String authCode = request.getHeader("auth_code");
        String dishName = request.getHeader("dish_name");

        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        MenuInfo menu = restaurantManager.getMenu(restaurantID, errorOs);
        if (menu == null)
            return TaskResponse.Companion.failed(failedReason.toString()).toJson();

        List<Integer> coordinate = holder.toIntList(2, ",", targetDish, errorOs);
        if (coordinate == null)
            return TaskResponse.Companion.failed(failedReason.toString()).toJson();

        Dish dish = Objects.requireNonNull(menu.getCategory(coordinate.get(0))).getDish(coordinate.get(1));
        if (dish == null)
            return TaskResponse.Companion.failed("找不到目标菜品").toJson();

        String oldPicUrl = dish.getPicUrl();
        if (!oldPicUrl.startsWith("EXAMPLE/")) if (!pictureManager.removeUploadedFile(oldPicUrl, errorOs))
            return TaskResponse.Companion.failed("原图片删除失败").toJson();

        dish.setPicUrl(pictureManager.saveUploadedPicture(filePart, restaurantID, "Dishes", dishName));

        if (restaurantManager.setMenu(restaurantID, new AuthCode(authCode), menu, errorOs))
            return TaskResponse.Companion.success().toJson();
        else
            return TaskResponse.Companion.failed(failedReason.toString()).toJson();


    }
}
