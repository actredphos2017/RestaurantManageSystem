package com.sakuno.restaurantmanagesystem.controller;


import com.sakuno.restaurantmanagesystem.json.restaurant.RestaurantRegisterInfo;
import com.sakuno.restaurantmanagesystem.managers.FileManager;
import com.sakuno.restaurantmanagesystem.managers.RestaurantManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@SpringBootApplication
@Controller
public class RegisterController {

    @GetMapping("/register")
    public String doGet() {
        return "register";
    }

    @PostMapping("/register")
    public String doPost(HttpServletRequest request, Model model) throws ServletException, IOException {

        Part filePart = request.getPart("headPic");
        String id = request.getParameter("id");
        String headPicPath = FileManager.saveUploadedFile(filePart, id);

        var registerInfo = new RestaurantRegisterInfo(
                request.getParameter("address"),
                request.getParameter("commit"),
                headPicPath, id,
                request.getParameter("managePassword"),
                request.getParameter("name"),
                request.getParameter("phone")
        );

        var manager = new RestaurantManager();

        if (manager.register(registerInfo))
            return "redirect:hello";
        else {
            model.addAttribute("failedReason", manager.popFailReason());
            return "register";
        }
    }
}
