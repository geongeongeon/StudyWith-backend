package com.studywith.api.global.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageService {

    @Value("${spring.image.base-path}")
    private String prefix;

    public String upload(MultipartFile image, String relativePath) throws IOException {
        String fileName = image.getOriginalFilename();
        String fileFormat = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();

        String path = prefix + relativePath;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path + uuid + fileFormat);
        image.transferTo(file);

        return relativePath + uuid + fileFormat;
    }

    public boolean delete(String relativePath) {
        String path = prefix + relativePath;
        File file = new File(path);

        return file.exists() && file.delete();
    }

}
