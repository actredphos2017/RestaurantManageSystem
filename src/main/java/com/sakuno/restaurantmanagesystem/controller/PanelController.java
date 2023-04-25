package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantFullData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PanelController {

    @GetMapping("/panel")
    public String doGet(HttpServletRequest request, Model model, @ModelAttribute("loginAccount") RestaurantFullData account) {
        model.addAttribute("restaurantName", account.getName());
        return "panel";
    }

    @PostMapping("/panel")
    public String doPost() {
        return "hello";
    }


}
