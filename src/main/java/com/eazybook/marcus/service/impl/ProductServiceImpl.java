package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.dto.ProductRequestDto;
import com.eazybook.marcus.dto.ProductResponseDto;
import com.eazybook.marcus.entity.Product;
import com.eazybook.marcus.repository.ProductRepository;
import com.eazybook.marcus.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    @Value("${product.upload.dir:uploads/products}")
    private String uploadDir;


    @Cacheable("products")
    @Override
    public List<ProductResponseDto> getProducts() {
        System.out.println("ProductService: getProducts");
        return productRepository.findAll()
                .stream().map(this::transformToDTO).collect(Collectors.toList());
    }

    private ProductResponseDto transformToDTO(Product product) {
        ProductResponseDto productDto = new ProductResponseDto();
        BeanUtils.copyProperties(product, productDto);
        productDto.setId(product.getId()); // <-- to‘g‘ri obyektga set qilindi
        return productDto;
    }



    @Override
    public ProductResponseDto addProduct(ProductRequestDto dto) {
        System.out.println("ProductService: addProduct");
        boolean exists = productRepository
                .existsByNameAndAuthor(dto.getName(), dto.getAuthor());

        if (exists) {
            throw new RuntimeException("Product already exists!");
        }

        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setAuthor(dto.getAuthor());
        product.setPublishedDate(dto.getPublishedDate());
        product.setLanguage(dto.getLanguage());
        product.setPages(dto.getPages());
        product.setStock(dto.getStock());
        product.setGenre(dto.getGenre());
        product.setPopularity(0);

        //  IMAGE HANDLE
        MultipartFile image = dto.getImage();

        if (image != null && !image.isEmpty()) {
            try {
                String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
                String fileName = UUID.randomUUID() + "." + extension;

                // papka yaratish
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // file path
                Path filePath = uploadPath.resolve(fileName);

                // file save
                Files.copy(image.getInputStream(), filePath);

                // DB ga URL saqlaymiz
                product.setImageUrl("/uploads/products/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        //  SAVE
        Product savedProduct = productRepository.save(product);

        //  DTO qaytarish
        return transformToDTO(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        System.out.println("ProductService: deleteProduct");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

}
