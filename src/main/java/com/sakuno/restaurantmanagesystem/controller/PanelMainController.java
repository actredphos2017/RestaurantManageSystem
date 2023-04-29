package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantFullData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PanelMainController {

    @GetMapping("/panel_main")
    public String doGet(HttpServletRequest request, Model model) {
        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        model.addAttribute("title", account.getName() + " 管理面板");
        model.addAttribute("restaurantName", account.getName());
        System.out.println(account.getName());
        return "panel_main";
    }

    @PostMapping("/panel_main")
    public String doPost() {
        return "hello";
    }


}
