package com.sakuno.restaurantmanagesystem.controller.clientapi;

import com.google.gson.Gson;
import com.sakuno.restaurantmanagesystem.model.customer.CustomerGetRestaurantInfo;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RestController
public class RestaurantDataGetController {

    @Autowired
    RestaurantManager manager;

    CustomerGetRestaurantInfo.Companion getResult = CustomerGetRestaurantInfo.Companion;

    @GetMapping("/restaurant/{id}")
    public String doGet(@PathVariable String id) {
        System.out.println("有人在获取餐厅信息：" + id);
        return new Gson().toJson(getInfo(id));
    }

    private CustomerGetRestaurantInfo getInfo(String id) {
        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        var showData = manager.getShowData(id, errorOs);
        if (showData == null) return getResult.failed(failedReason.toString());
        else return getResult.success(showData);
    }
}
