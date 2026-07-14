package com.eazybook.marcus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"customer_id", "product_id"})
        }
)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    // Qashan men like arqali basqa magliwmatlar kerek bolsa qosaman.
    //  (optional) relationship qo‘shmoqchi bo‘lsang:
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_id", insertable = false, updatable = false)
    // private Product product;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    // private Customer customer;
}
