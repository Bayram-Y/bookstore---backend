package com.eazybook.marcus.service;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    String save(MultipartFile file);
    void delete(String fileName);
}
