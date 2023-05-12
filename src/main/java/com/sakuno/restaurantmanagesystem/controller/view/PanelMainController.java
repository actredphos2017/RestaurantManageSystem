package com.sakuno.restaurantmanagesystem.controller.view;

import com.sakuno.restaurantmanagesystem.model.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PanelMainController {

    @Autowired
    RestaurantManager manager;

    @GetMapping("/panel_main")
    public String doGet(HttpServletRequest request, Model model) {

        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        if (account == null) return "error";

        model.addAttribute("account", account);
        System.out.println(account.getName());
        return "panel_main";
    }

    @PostMapping("/panel_main")
    public String doPost() {
        return "error";
    }


}
