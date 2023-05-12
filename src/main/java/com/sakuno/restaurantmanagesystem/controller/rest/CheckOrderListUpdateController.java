package com.sakuno.restaurantmanagesystem.controller.rest;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.sakuno.restaurantmanagesystem.Global;
import com.sakuno.restaurantmanagesystem.model.menu.MenuInfo;
import com.sakuno.restaurantmanagesystem.service.RestaurantManager;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RestController
public class CheckOrderListUpdateController {

    @Autowired
    RestaurantManager restaurantManager;

    @GetMapping("/rest/order_update/{id}")
    public String doGet(@PathVariable String id) {
        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        MenuInfo menu = restaurantManager.getMenu(id, errorOs);

        if (menu == null)
            return new Gson().toJson(Response.failed());
        else
            return new Gson().toJson(Response.success(Global.getInstance().orderUpdated(id)));
    }

    static class Response {
        @SerializedName("ok")
        public boolean ok;

        @SerializedName("result")
        @Nullable
        public Boolean result;

        private Response(boolean ok, @Nullable Boolean result) {
            this.ok = ok;
            this.result = result;
        }

        public static Response success(boolean result) {
            return new Response(true, result);
        }

        public static Response failed() {
            return new Response(false, null);
        }
    }
}
