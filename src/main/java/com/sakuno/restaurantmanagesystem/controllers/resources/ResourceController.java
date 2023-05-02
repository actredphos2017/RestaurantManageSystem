package com.sakuno.restaurantmanagesystem.controllers.resources;

import com.sakuno.restaurantmanagesystem.manager.PictureManager;
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
    PictureManager pictureManager;

    @Autowired
    RestaurantManager restaurantManager;

    // 图片url转图片
    @GetMapping("/resource/{id}/Dishes/{dish_name}")
    public ResponseEntity<Resource> getPictureRes(@PathVariable String id, @PathVariable String dish_name) throws FileNotFoundException {

        File pictureFile = new File(pictureManager.getFullPath("/resource/" + id + "/Dishes/" + dish_name));

        if (!pictureFile.exists())
            throw new FileNotFoundException("Picture " + dish_name + " Not Found!");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                .body(new FileSystemResource(pictureFile));
    }

    @GetMapping("/resource/{id}/headpic")
    public ResponseEntity<Resource> getHeadPictureRes(@PathVariable String id) throws FileNotFoundException {

        ByteArrayOutputStream failedReason = new ByteArrayOutputStream();
        PrintStream errorOs = new PrintStream(failedReason);

        String picUrl = restaurantManager.getHeadPic(id, errorOs);
        if (picUrl == null)
            throw new FileNotFoundException("Restaurant ID " + id + " Not Found!");

        File pictureFile = new File(pictureManager.getFullPath(picUrl));

        if (!pictureFile.exists())
            throw new FileNotFoundException("Head Picture " + id + " Not Found!");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                .body(new FileSystemResource(pictureFile));
    }
}
