package com.sakuno.restaurantmanagesystem.controller;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Controller
public class TestController {
    @GetMapping("/index")
    public String hello(Model model) {
        model.addAttribute("title", "导入的Title");
        return "index";
    }
}
