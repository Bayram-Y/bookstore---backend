package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.exception.ImageUploadException;
import com.eazybook.marcus.exception.InvalidFileException;
import com.eazybook.marcus.service.IImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements IImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public String save(MultipartFile file) {
        validate(file);

        try {
            String fileName = generateFileName(file.getOriginalFilename());

            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

             return  fileName;

        } catch (IOException e) {
            log.error("Image upload failed", e);
            throw new ImageUploadException("Image upload failed");
        }
    }

    @Override
    public void delete(String fileName) {
        if (fileName == null) return;

        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Image delete failed: {}", fileName, e);
        }
    }

    // ================= PRIVATE HELPERS =================

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new InvalidFileException("Max file size is 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("Only image files allowed");
        }
    }

    private String generateFileName(String originalName) {
        String extension = Optional.ofNullable(originalName)
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".") + 1))
                .orElse("jpg");

        return UUID.randomUUID() + "." + extension;
    }
}

