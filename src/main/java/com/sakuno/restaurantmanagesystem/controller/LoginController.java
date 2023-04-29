package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantLoginInfo;
import com.sakuno.restaurantmanagesystem.manager.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

@SpringBootApplication
@Controller
public class LoginController {

    @Autowired
    RestaurantManager manager;

    @GetMapping("/login")
    public String doGet() {
        return "login";
    }

    @PostMapping("/login")
    public String doPost(HttpServletRequest request, Model model) {

        RestaurantLoginInfo info = new RestaurantLoginInfo(
                request.getParameter("password"),
                request.getParameter("username")
        );

        OutputStream failReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failReason);

        var loginRes = manager.login(info, errorOs);

        if (loginRes == null) {
            model.addAttribute("failedReason", failReason.toString());
            return "login";
        } else {
            request.getSession().setAttribute("loginAccount", loginRes);
            return "redirect:panel_main";
        }
    }
}
