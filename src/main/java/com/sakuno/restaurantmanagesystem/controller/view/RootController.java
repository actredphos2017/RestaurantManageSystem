package com.sakuno.restaurantmanagesystem.controller.view;

import com.sakuno.restaurantmanagesystem.model.restaurant.RestaurantFullData;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

@SpringBootApplication
@Controller
public class RootController {

    @Autowired
    RestaurantManager manager;

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

        OutputStream failReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failReason);

        try {
            var cookies = request.getCookies();
            if (cookies == null) throw new Exception();

            for (var cookie : cookies)
                if (Objects.equals(cookie.getName(), "authCode")) {
                    var authCode = cookie.getValue();

                    autoLoginAccount = manager.checkAuthCode(authCode, errorOs);
                    if (autoLoginAccount == null) {
                        alertMsg(failReason.toString());
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
