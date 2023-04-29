package com.sakuno.restaurantmanagesystem.manager;

import jakarta.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@PropertySource("classpath:application.properties")
public class FileManager {

    @Value("${com.sakuno.fileManager.path}")
    String folderPath;

    public String saveUploadedFile(Part filePart, String folder, String prefix) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(filePart.getInputStream());

        if ("png".equalsIgnoreCase(FilenameUtils.getExtension(Paths.get(filePart.getSubmittedFileName()).getFileName().toString()))) {
            BufferedImage tempBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            tempBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            bufferedImage = tempBufferedImage;
        }

        String finalFolderPath = folderPath + folder + "/";
        String fileName = prefix + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "." + "jpg";
        String filePath = finalFolderPath + fileName;

        Path path = Paths.get(finalFolderPath);
        if (!Files.exists(path)) Files.createDirectories(path);

        File outputFile = new File(filePath);

        ImageIO.write(bufferedImage, "jpg", outputFile);

        return filePath;
    }

    public boolean removeUploadedFile(String path, PrintStream errorOs) {
        File target = new File(path);

        if (!target.exists()) {
            errorOs.println("目标文件不存在！");
            return false;
        }

        if (target.delete())
            return true;
        else {
            errorOs.println("删除失败，请检查后台权限");
            return false;
        }
    }
}
