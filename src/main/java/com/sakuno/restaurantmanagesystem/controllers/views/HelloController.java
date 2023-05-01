package com.sakuno.restaurantmanagesystem.controllers.views;


import com.google.gson.Gson;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantFullData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
@Controller
public class HelloController {

    @GetMapping("/hello")
    public String doGet(HttpServletRequest request, Model model, @ModelAttribute("loginAccount") RestaurantFullData account) {
        return doPost(request, model, account);
    }

    @PostMapping("/hello")
    public String doPost(HttpServletRequest request, Model model, @ModelAttribute("loginAccount") RestaurantFullData account) {
        model.addAttribute("title", "登录账号");
        model.addAttribute("msg", new Gson().toJson(account));
        model.addAttribute("headPic", "/resource/" + account.getId() + "/headpic");
        return "hello";
    }
}
