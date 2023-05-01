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
public class PictureManager {

    @Value("${com.sakuno.fileManager.path}")
    String folderPath;

    public FilePathInfo assignPath(String restaurantID, String category, String prefix) {
        if (prefix == null)
            return new FilePathInfo(restaurantID, category + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        else
            return new FilePathInfo(restaurantID + "/" + category, prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    }

    static class FilePathInfo {
        private final String folderPath;

        public String getFolderPath() {
            return folderPath;
        }

        private final String fileName;

        public String getFileName() {
            return fileName;
        }

        public String getFullPath() {
            if (folderPath.endsWith("/"))
                return folderPath + fileName;
            else return folderPath + "/" + fileName;
        }

        public FilePathInfo(String folderPath, String fileName) {
            if (folderPath.startsWith("/"))
                this.folderPath = folderPath.substring(1);
            else
                this.folderPath = folderPath;

            if (fileName.endsWith(".jpg")) this.fileName = fileName;
            else this.fileName = fileName + ".jpg";
        }
    }

    public String saveUploadedPicture(Part filePart, String restaurantID, String category, String prefix) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(filePart.getInputStream());

        if ("png".equalsIgnoreCase(FilenameUtils.getExtension(Paths.get(filePart.getSubmittedFileName()).getFileName().toString()))) {
            BufferedImage tempBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            tempBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            bufferedImage = tempBufferedImage;
        }

        FilePathInfo filePathInfo = assignPath(restaurantID, category, prefix);

        Path path = Paths.get(filePathInfo.getFolderPath());
        if (!Files.exists(path)) Files.createDirectories(path);

        File outputFile = new File(filePathInfo.getFullPath());

        ImageIO.write(bufferedImage, "jpg", outputFile);

        return filePathInfo.getFullPath();
    }

    public String getFullPath(String relativePath) {
        if(relativePath.startsWith("/"))
            return folderPath + relativePath.substring(1);
        else
            return folderPath + relativePath;
    }

    public boolean removeUploadedFile(String path, PrintStream errorOs) {
        File target = new File(getFullPath(path));

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
