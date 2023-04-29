package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.manager.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

@Controller
public class PanelOrderController {

    @GetMapping("/panel_order")
    public String doGet(HttpServletRequest request, Model model) {
        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        model.addAttribute("title", account.getName() + " 订单管理");
        model.addAttribute("restaurantName", account.getName());
        return "panel_order";
    }

    @PostMapping("/panel_order")
    public String doPost() {
        return "hello";
    }


}
