package com.sakuno.restaurantmanagesystem.controller.view;

import com.sakuno.restaurantmanagesystem.model.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.model.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.service.PictureManager;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

@Controller
public class PanelMenuController {

    @Autowired
    RestaurantManager restaurantManager;

    @Autowired
    PictureManager pictureManager;

    @GetMapping("/panel_menu")
    public String doGet(HttpServletRequest request, Model model) throws IOException {
        var failedReason = new ByteArrayOutputStream();
        var errorOs = new PrintStream(failedReason);

        RestaurantFullData account = (RestaurantFullData) request.getSession().getAttribute("loginAccount");
        if (account == null) return "error";

        MenuInfo menu = restaurantManager.getMenu(account.getId(), errorOs);
        if (menu == null) return "error";

        ArrayList<ArrayList<String>> menuUrls = new ArrayList<>();
        menu.getCategories().forEach((category) -> {
            ArrayList<String> urlList = new ArrayList<>();
            category.getDishes().forEach((dish -> urlList.add(dish.getPicUrl())));
            menuUrls.add(urlList);
        });

        model.addAttribute("account", account);
        model.addAttribute("menu", menu);
        model.addAttribute("pic_urls", menuUrls);

        System.out.println("HelloWorld");

        return "panel_menu";
    }

    @PostMapping("/panel_menu")
    public String doPost() {
        return "error";
    }
}