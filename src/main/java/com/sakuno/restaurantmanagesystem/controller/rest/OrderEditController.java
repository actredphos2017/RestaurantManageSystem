package com.sakuno.restaurantmanagesystem.controller.rest;

import com.google.gson.Gson;
import com.sakuno.restaurantmanagesystem.model.rest.DefaultResponse;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RestController
public class OrderEditController {

    @Autowired
    RestaurantManager restaurantManager;

    @PostMapping("/rest/edit_order")
    public String onPost(HttpServletRequest request) {
        String requestHeader = request.getHeader("request");
        String restaurantID = request.getHeader("r_id");
        String orderID = request.getHeader("o_id");

        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        switch (requestHeader) {
            case "finish" -> {
                boolean success = restaurantManager.finishOrder(restaurantID, orderID, errorOs);
                if (success) return new Gson().toJson(DefaultResponse.Companion.success());
                else return new Gson().toJson(DefaultResponse.Companion.failed(failedReason.toString()));
            }
            case "delete" -> {
                boolean success = restaurantManager.deleteOrder(restaurantID, orderID, errorOs);
                if (success) return new Gson().toJson(DefaultResponse.Companion.success());
                else return new Gson().toJson(DefaultResponse.Companion.failed(failedReason.toString()));
            }
            default -> {
                return new Gson().toJson(DefaultResponse.Companion.failed("未知请求"));
            }
        }
    }
}