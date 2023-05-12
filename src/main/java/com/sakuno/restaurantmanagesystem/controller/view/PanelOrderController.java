package com.sakuno.restaurantmanagesystem.controller.view;

import com.sakuno.restaurantmanagesystem.Global;
import com.sakuno.restaurantmanagesystem.model.order.OrderFullInfo;
import com.sakuno.restaurantmanagesystem.model.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

@Controller
public class PanelOrderController {

    @Autowired
    RestaurantManager restaurantManager;

    @GetMapping("/panel_order")
    public String doGet(HttpServletRequest request, Model model) {
        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        if (account == null) return "error";

        model.addAttribute("account", account);

        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);
        List<OrderFullInfo> orders = restaurantManager.getOrders(account.getId(), false, errorOs);
        if (orders.isEmpty())
            model.addAttribute("error", failedReason.toString());

        model.addAttribute("orders", orders);
        Global.getInstance().discoverUpdate(account.getId());
        return "panel_order";
    }
}