package com.sakuno.restaurantmanagesystem.controllers.views;

import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantFullData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PanelOrderController {

    @GetMapping("/panel_order")
    public String doGet(HttpServletRequest request, Model model) {
        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        if (account == null) return "error";

        model.addAttribute("account", account);
        return "panel_order";
    }

    @PostMapping("/panel_order")
    public String doPost() {
        return "error";
    }


}
