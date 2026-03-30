package com.eazybook.marcus.dto;

import com.eazybook.marcus.enums.Category;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductUpdateRequestDto {

    @Size(min = 2, max = 250, message = "Name must be between 2 and 250 characters")
    private String name;

    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    private MultipartFile image;

    @Size(max = 200, message = "Author name cannot exceed 200 characters")
    private String author;

    @PastOrPresent(message = "Published date cannot be in the future")
    private LocalDate publishedDate;

    @Size(max = 50, message = "Language cannot exceed 50 characters")
    private String language;

    @PositiveOrZero(message = "Pages must be zero or positive")
    private Integer pages;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    private Category category;
}