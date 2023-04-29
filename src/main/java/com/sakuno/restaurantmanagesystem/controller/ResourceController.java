package com.sakuno.restaurantmanagesystem.controller;

import com.sakuno.restaurantmanagesystem.manager.RestaurantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;

@Controller
public class ResourceController {

    @Autowired
    RestaurantManager manager;

    @GetMapping("/resource/{restaurantID}/headpic")
    public ResponseEntity<Resource> getRestaurantHeadPic(@PathVariable String restaurantID) throws FileNotFoundException {
        OutputStream failReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failReason);

        File headPicFile = new File(manager.getHeadPic(restaurantID, errorOs));

        if (!headPicFile.exists())
            throw new FileNotFoundException("File " + headPicFile + " Not Found!");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                .body(new FileSystemResource(headPicFile));
    }
}
