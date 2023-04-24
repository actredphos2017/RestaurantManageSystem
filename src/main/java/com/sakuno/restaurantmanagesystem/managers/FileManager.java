package com.sakuno.restaurantmanagesystem.managers;

import jakarta.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {

    public static String saveUploadedFile(Part filePart, String prefix) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(filePart.getInputStream());

        if ("png".equalsIgnoreCase(FilenameUtils.getExtension(Paths.get(filePart.getSubmittedFileName()).getFileName().toString()))) {
            BufferedImage tempBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            tempBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            bufferedImage = tempBufferedImage;
        }

        String folderPath = "D:/uploads/";
        String fileName = prefix + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "." + "jpg";
        String filePath = folderPath + fileName;

        Path folder = Paths.get(folderPath);
        if (!Files.exists(folder)) Files.createDirectories(folder);

        File outputFile = new File(filePath);

        ImageIO.write(bufferedImage, "jpg", outputFile);

        return filePath;
    }
}
