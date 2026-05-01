package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.dto.ProductRequestDto;
import com.eazybook.marcus.dto.ProductResponseDto;
import com.eazybook.marcus.dto.ProductUpdateRequestDto;
import com.eazybook.marcus.entity.Product;
import com.eazybook.marcus.repository.ProductRepository;
import com.eazybook.marcus.service.IProductService;
import com.eazybook.marcus.util.ImageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ImageServiceImpl imageServiceImpl;
    @Value("${product.upload.dir:uploads/products}")
    private String uploadDir;
    private static final String IMAGE_PATH = "/uploads/products/";


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
    public ProductResponseDto getProduct(Long id) {
        System.out.println("ProductService: getProduct");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return transformToDTO(product);
    }


    @Transactional
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponseDto addProduct(ProductRequestDto dto) {
        System.out.println("ProductService: addProduct");

        String fileName = null;

        try {
            //  Image save
            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                fileName = imageServiceImpl.save(dto.getImage());
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
        product.setCategory(dto.getCategory());
        product.setPopularity(0);

         if (fileName != null) {
                product.setImageUrl(IMAGE_PATH + fileName);
            }

            Product savedProduct = productRepository.save(product);

            return transformToDTO(savedProduct);

        } catch (Exception e) {

            //  rollback file
            if (fileName != null) {
                imageServiceImpl.delete(fileName);
            }

            throw e;
        }
    }
    @Transactional
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        System.out.println("ProductService: deleteProduct");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        //  image delete
        if (product.getImageUrl() != null) {
            try {
                Path oldPath = Paths.get(uploadDir,
                        Paths.get(product.getImageUrl()).getFileName().toString());
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image", e);
            }
        }

        productRepository.delete(product);
    }

    @Transactional
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto dto) {
        log.info("Updating product with id: {}", id);

        //  1.find Product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        //  2. Duplicate check (name + author)
        String name = dto.getName() != null ? dto.getName() : product.getName();
        String author = dto.getAuthor() != null ? dto.getAuthor() : product.getAuthor();

        if (productRepository.existsByNameAndAuthorAndIdNot(name, author, id)) {
            throw new RuntimeException("Product already exists!");
        }

        //  3. FIELD UPDATE
          updateFields(product, dto);

        String newFileName = null;
        String oldImage = product.getImageUrl();

        // IMAGE UPDATE
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            ImageValidator.validate(dto.getImage());

            try {
                newFileName = imageServiceImpl.save(dto.getImage());
                product.setImageUrl(IMAGE_PATH + newFileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update image", e);
            }
        }

        // SAVE (SAFE)
        Product updatedProduct;
        try {
            updatedProduct = productRepository.save(product);
        } catch (Exception e) {
            //  rollback file
            if (newFileName != null) {
                try {
                    Path newPath = Paths.get(uploadDir, newFileName);
                    Files.deleteIfExists(newPath);
                } catch (IOException ex) {
                    log.error("Failed to delete new image after DB error", ex);
                }
            }
            throw e;
        }

        // DELETE OLD IMAGE
         deleteOldImage(oldImage,newFileName);

        // RETURN
        return transformToDTO(updatedProduct);
    }

    private void updateFields(Product product, ProductUpdateRequestDto dto) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getAuthor() != null) product.setAuthor(dto.getAuthor());
        if (dto.getPublishedDate() != null) product.setPublishedDate(dto.getPublishedDate());
        if (dto.getLanguage() != null) product.setLanguage(dto.getLanguage());
        if (dto.getPages() != null) product.setPages(dto.getPages());
        if (dto.getStock() != null) product.setStock(dto.getStock());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
    }

    private void deleteOldImage(String oldImage, String newFileName) {
        if (newFileName != null && oldImage != null &&
                !oldImage.equals(IMAGE_PATH + newFileName)) {
            try {
                String oldFileName = Paths.get(oldImage).getFileName().toString();
                Path oldPath = Paths.get(uploadDir, oldFileName);
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                log.warn("Failed to delete old image");
            }
        }
    }
 }
