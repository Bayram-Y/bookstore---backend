package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.dto.ProductRequestDto;
import com.eazybook.marcus.dto.ProductResponseDto;
import com.eazybook.marcus.dto.ProductUpdateRequestDto;
import com.eazybook.marcus.entity.Product;
import com.eazybook.marcus.repository.ProductRepository;
import com.eazybook.marcus.service.IProductService;
import com.eazybook.marcus.util.ImageValidator;
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
import java.util.Optional;
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
    public ProductResponseDto getProduct(Long id) {
        System.out.println("ProductService: getProduct");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return transformToDTO(product);
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
        product.setCategory(dto.getCategory());
        product.setPopularity(0);

        //  IMAGE HANDLE
        MultipartFile image = dto.getImage();

        if (image != null && !image.isEmpty()) {
            ImageValidator.validate(dto.getImage());
            try {
                String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
                String fileName = UUID.randomUUID() + "." + extension;

                // create folder
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // file path
                Path filePath = uploadPath.resolve(fileName);

                // file save
                Files.copy(image.getInputStream(), filePath);

                // into DB  URL save
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

    @Override
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto dto) {
        System.out.println("ProductService: updateProduct");

        //  1.find Product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        //  2. Duplicate check (name + author)
        String name = dto.getName() != null ? dto.getName() : product.getName();
        String author = dto.getAuthor() != null ? dto.getAuthor() : product.getAuthor();

        boolean exists = productRepository
                .existsByNameAndAuthorAndIdNot(name, author, id);

        if (exists) {
            throw new RuntimeException("Product already exists!");
        }


        //  3. FIELD UPDATE

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getAuthor() != null) {
            product.setAuthor(dto.getAuthor());
        }

        if (dto.getPublishedDate() != null) {
            product.setPublishedDate(dto.getPublishedDate());
        }

        if (dto.getLanguage() != null) {
            product.setLanguage(dto.getLanguage());
        }

        if (dto.getPages() != null) {
            product.setPages(dto.getPages());
        }

        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }

        if (dto.getCategory() != null) {
            product.setCategory(dto.getCategory());
        }

        //  4. IMAGE UPDATE
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            ImageValidator.validate(dto.getImage());
            try {
                // delete old image
                if (product.getImageUrl() != null) {
                    Path oldPath = Paths.get(uploadDir,
                            Paths.get(product.getImageUrl()).getFileName().toString());
                    boolean deleted = Files.deleteIfExists(oldPath);
                    System.out.println("Old image deleted: " + deleted);
                }

                // new image
                String extension = StringUtils.getFilenameExtension(dto.getImage().getOriginalFilename());
                if (extension == null) {
                    throw new RuntimeException("Invalid file extension");
                }
                String fileName = UUID.randomUUID() + "." + extension;



                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(dto.getImage().getInputStream(), filePath);

                product.setImageUrl("/uploads/products/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to update image", e);
            }
        }

        //  5. SAVE
        Product updatedProduct = productRepository.save(product);

        //  6. RETURN
        return transformToDTO(updatedProduct);
    }

}
