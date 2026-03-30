package com.eazybook.marcus.util;


import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageValidator {

    public static void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return; // optional image
        }

        String contentType = file.getContentType();
        long maxSize = 2 * 1024 * 1024; // 2MB

        // TYPE CHECK
        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png"))) {
            throw new RuntimeException("Only JPG, JPEG, PNG images are allowed!");
        }

        // SIZE CHECK
        if (file.getSize() > maxSize) {
            throw new RuntimeException("Image size must be less than 2MB!");
        }

        //  REAL IMAGE CHECK
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());

            if (image == null) {
                throw new RuntimeException("Fake image detected!");
            }

        } catch (IOException e) {
            throw new RuntimeException("Invalid image file", e);
        }
    }
}
