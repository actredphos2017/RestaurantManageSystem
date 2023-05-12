package com.sakuno.restaurantmanagesystem.controller.resource;

import com.sakuno.restaurantmanagesystem.service.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QRCodeController {

    @Autowired
    QRCodeGenerator generator;

    @GetMapping("/rest/seat_qrcode/{restaurant_id}/{seat}")
    public ResponseEntity<Resource> doPost(@PathVariable String restaurant_id, @PathVariable String seat) {
        if (restaurant_id == null || seat == null) throw new RuntimeException();
        if (restaurant_id.isEmpty() || seat.isEmpty()) throw new RuntimeException();

        byte[] qrCodeJPG = generator.generate(restaurant_id + '|' + seat);

        if (qrCodeJPG == null) throw new RuntimeException();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
                .body(new ByteArrayResource(qrCodeJPG));
    }
}
