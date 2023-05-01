package com.sakuno.restaurantmanagesystem.controllers.views;


import com.sakuno.restaurantmanagesystem.dataclasses.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantRegisterInfo;
import com.sakuno.restaurantmanagesystem.manager.RestaurantManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

@SpringBootApplication
@Controller
public class RegisterController {

    @Autowired
    RestaurantManager manager;

    @GetMapping("/register")
    public String doGet() {
        return "register";
    }

    @PostMapping("/register")
    public String doPost(HttpServletRequest request, Model model) throws ServletException, IOException {
        OutputStream failReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failReason);

        Part filePart = request.getPart("headPic");
        String id = request.getParameter("id");

        var registerInfo = new RestaurantRegisterInfo(
                request.getParameter("address"),
                request.getParameter("commit"),
                null, id,
                request.getParameter("managePassword"),
                request.getParameter("name"),
                request.getParameter("phone")
        );

        var registerResult = manager.register(registerInfo, filePart, MenuInfo.Companion.getExample(), errorOs);

        if (registerResult != null) {
            request.getSession().setAttribute("loginAccount", registerResult);
            return "redirect:panel_main";
        } else {
            model.addAttribute("failedReason", failReason.toString());
            return "register";
        }
    }
}
