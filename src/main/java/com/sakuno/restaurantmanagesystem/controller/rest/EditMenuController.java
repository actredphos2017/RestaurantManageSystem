package com.sakuno.restaurantmanagesystem.controller.rest;

import com.sakuno.restaurantmanagesystem.model.rest.DefaultResponse;
import com.sakuno.restaurantmanagesystem.service.MenuRequestHolder;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RestController
public class EditMenuController {

    @Autowired
    RestaurantManager manager;

    @Autowired
    MenuRequestHolder requestHolder;

    @GetMapping("/rest/edit_menu")
    public String doGet(HttpServletRequest request) {
        return doPost(request);
    }

    @PostMapping("/rest/edit_menu")
    public String doPost(HttpServletRequest request) {

        String authCode = request.getHeader("auth_code");
        String id = request.getHeader("id");
        String taskName = request.getHeader("task_name");

        String body = null;

        try {
            BufferedReader bodyReader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bodyReader.readLine()) != null) builder.append(line);
            body = builder.toString();
        } catch (Exception ignore) {
        }


        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        return new DefaultResponse(requestHolder.holder(taskName, id, authCode, body == null ? "" : body, errorOs), failedReason.toString()).toJson();
    }
}
