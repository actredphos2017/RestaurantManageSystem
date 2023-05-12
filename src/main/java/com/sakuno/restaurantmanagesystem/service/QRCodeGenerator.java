package com.sakuno.restaurantmanagesystem.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class QRCodeGenerator {

    private static final String format = "JPG";
    private static final int size = 256;

    public byte[] generate(String content) {
        try {
            var matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size);
            var byteArrayOutputStreamStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, format, byteArrayOutputStreamStream);
            return byteArrayOutputStreamStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}