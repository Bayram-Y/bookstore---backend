package com.eazybook.marcus.dto;

import com.eazybook.marcus.enums.Category;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductUpdateRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private MultipartFile image;
    private String author;
    private LocalDate publishedDate;
    private String language;
    private Integer pages;
    private Integer stock;
    private Category category;
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100")
    private BigDecimal discountPercent;
    int likesCount;
    boolean isLikedByCurrentUser;

}