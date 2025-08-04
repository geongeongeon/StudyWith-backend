package com.studywith.api.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public String getImagePrefix() {
        return "https://%s.s3.%s.amazonaws.com/".formatted(bucket, region);
    }

    public String upload(MultipartFile image, String relativePath) throws IOException {
        String originalName = image.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();

        String key = relativePath + uuid + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(image.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(image.getBytes()));

        return getImagePrefix() + key;
    }

    public boolean delete(String fullUrl) {
        try {
            if (fullUrl == null || fullUrl.contains("/default")) {
                return false;
            }

            String key = fullUrl.replace(getImagePrefix(), "");

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
