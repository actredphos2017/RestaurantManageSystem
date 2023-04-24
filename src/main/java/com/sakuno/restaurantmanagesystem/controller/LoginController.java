package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantLoginInfo;
import com.sakuno.restaurantmanagesystem.managers.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringBootApplication
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String doGet() {
        return "login";
    }

    @PostMapping("/login")
    public String doPost(RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {

        logger.debug("Start Login! User: " + request.getParameter("username") + " Password: " + request.getParameter("password"));

        RestaurantLoginInfo info = new RestaurantLoginInfo(
                request.getParameter("password"),
                request.getParameter("username")
        );

        var manager = new RestaurantManager();

        var loginRes = manager.login(info);

        if (loginRes == null) {
            model.addAttribute("failedReason", manager.popFailReason());
            return "login";
        } else {
            redirectAttributes.addFlashAttribute("loginAccount", loginRes);
            return "redirect:hello";
        }
    }
}
