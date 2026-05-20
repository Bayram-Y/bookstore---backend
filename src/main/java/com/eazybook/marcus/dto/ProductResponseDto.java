package com.eazybook.marcus.dto;

import com.eazybook.marcus.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDto {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer popularity;
    private String imageUrl;
    private String author;
    private LocalDate publishedDate;
    private String language;
    private Integer pages;
    private Integer stock;
    private Category category;
    // 🔥 ADD QILINDI
    private BigDecimal discountPercent;
    private BigDecimal discountPrice;

}