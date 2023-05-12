package com.sakuno.restaurantmanagesystem.controller.clientapi;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sakuno.restaurantmanagesystem.Global;
import com.sakuno.restaurantmanagesystem.model.order.OrderDetail;
import com.sakuno.restaurantmanagesystem.model.order.OrderFullInfo;
import com.sakuno.restaurantmanagesystem.model.rest.OrderPushResponse;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@RestController
public class OrderPushController {

    @Autowired
    RestaurantManager restaurantManager;

    @PostMapping("/restaurant/{id}/pushorder")
    public String onPost(@PathVariable String id, HttpServletRequest request) {
        String body;
        OrderDetail detail;

        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        try {
            BufferedReader bodyReader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bodyReader.readLine()) != null) builder.append(line);
            body = builder.toString();
            detail = new Gson().fromJson(body, OrderDetail.class);
            if (detail == null) throw new JsonSyntaxException("");

            OrderFullInfo info = restaurantManager.pushOrder(id, detail, errorOs);
            if (info == null) return OrderPushResponse.Companion.failed(failedReason.toString()).toJson();

            Global.getInstance().updateOrder(id);
            return OrderPushResponse.Companion.success(info.getMoney(), info.getOId(), info.getFailedList()).toJson();
        } catch (IOException ignore) {
            return OrderPushResponse.Companion.failed("缺少请求体！").toJson();
        } catch (JsonSyntaxException ignore) {
            return OrderPushResponse.Companion.failed("请求格式错误！").toJson();
        }
    }
}
