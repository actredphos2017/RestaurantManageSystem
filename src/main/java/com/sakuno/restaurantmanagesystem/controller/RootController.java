package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.managers.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@SpringBootApplication
@Controller
public class RootController {

    @GetMapping("/")
    public String doGet(HttpServletRequest request, Model model) {
        return doPost(request, model);
    }

    @PostMapping("/")
    public String doPost(HttpServletRequest request, Model model) {
        if (checkLoginStatus(request))
            return "redirect:main";
        else return "redirect:login";
    }

    private RestaurantFullData autoLoginAccount = null;

    public boolean checkLoginStatus(HttpServletRequest request) {
        try {
            var cookies = request.getCookies();
            if (cookies == null) throw new Exception();

            var manager = new RestaurantManager();

            for (var cookie : cookies)
                if (Objects.equals(cookie.getName(), "authCode")) {
                    var authCode = cookie.getValue();

                    autoLoginAccount = manager.checkAuthCode(authCode);
                    if (autoLoginAccount == null) {
                        alertMsg(manager.popFailReason());
                        return false;
                    } else return true;
                }
        } catch (Exception ignore) {
        }
        return false;
    }

    private void alertMsg(String msg) {

    }
}
