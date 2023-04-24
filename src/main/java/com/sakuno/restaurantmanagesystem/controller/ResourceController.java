package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.managers.RestaurantManager;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileNotFoundException;

@Controller
public class ResourceController {
    @GetMapping("/resource/{restaurantID}/headpic")
    public ResponseEntity<Resource> getRestaurantHeadPic(@PathVariable String restaurantID) throws FileNotFoundException {
        var manager = new RestaurantManager();
        File headPicFile = new File(manager.getHeadPic(restaurantID));

        if (!headPicFile.exists())
            throw new FileNotFoundException("File " + headPicFile + " Not Found!");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                .body(new FileSystemResource(headPicFile));
    }
}
