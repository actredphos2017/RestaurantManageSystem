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
import java.io.PrintStream;

@Controller
public class PanelMenuController {

    @Autowired
    RestaurantManager manager;
    @GetMapping("/panel_menu")
    public String doGet(HttpServletRequest request, Model model) {
        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        model.addAttribute("title", account.getName() + " 菜单管理");
        model.addAttribute("restaurantName", account.getName());
        var failedReason = new ByteArrayOutputStream();
        var errorOs = new PrintStream(failedReason);


        model.addAttribute("menu", manager.getMenu(account.getId(), errorOs));
        return "panel_menu";
    }

    @PostMapping("/panel_menu")
    public String doPost() {
        return "hello";
    }


}
