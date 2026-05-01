package com.eazybook.marcus.entity;

import com.eazybook.marcus.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "products",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "author"})
        })
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "popularity", nullable = false)
    private Integer popularity;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Book field
    @Column(name = "author", nullable = false, length = 200)
    private String author;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;
}