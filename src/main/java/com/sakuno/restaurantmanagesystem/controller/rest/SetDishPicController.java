package com.sakuno.restaurantmanagesystem.controller.rest;

import com.sakuno.restaurantmanagesystem.model.menu.Dish;
import com.sakuno.restaurantmanagesystem.model.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.model.rest.DefaultResponse;
import com.sakuno.restaurantmanagesystem.service.MenuRequestHolder;
import com.sakuno.restaurantmanagesystem.service.PictureManager;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import com.sakuno.restaurantmanagesystem.util.AuthCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String onPost(@RequestParam("dish_name") String dishName, MultipartHttpServletRequest request) throws ServletException, IOException {
        Part filePart = request.getPart("picture");
        String targetDish = request.getHeader("target_dish");
        String restaurantID = request.getHeader("id");
        String authCode = request.getHeader("auth_code");

        System.out.println();

        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        MenuInfo menu = restaurantManager.getMenu(restaurantID, errorOs);
        if (menu == null)
            return DefaultResponse.Companion.failed(failedReason.toString()).toJson();

        List<Integer> coordinate = holder.toIntList(2, ",", targetDish, errorOs);
        if (coordinate == null)
            return DefaultResponse.Companion.failed(failedReason.toString()).toJson();

        Dish dish = Objects.requireNonNull(menu.getCategory(coordinate.get(0))).getDish(coordinate.get(1));
        if (dish == null)
            return DefaultResponse.Companion.failed("找不到目标菜品").toJson();

        String oldPicUrl = dish.getPicUrl();
        if (oldPicUrl != null)
            if (!(oldPicUrl.startsWith("EXAMPLE/") || oldPicUrl.isEmpty()))
                if (!pictureManager.removeUploadedFile(oldPicUrl, errorOs))
                    return DefaultResponse.Companion.failed("原图片删除失败").toJson();

        dish.setPicUrl(pictureManager.saveUploadedPicture(filePart, restaurantID, "Dishes", dishName));

        if (restaurantManager.setMenu(restaurantID, new AuthCode(authCode), menu, errorOs))
            return DefaultResponse.Companion.success().toJson();
        else
            return DefaultResponse.Companion.failed(failedReason.toString()).toJson();


    }
}
