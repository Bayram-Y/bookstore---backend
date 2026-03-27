package com.eazybook.marcus.repository;

import com.eazybook.marcus.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameAndAuthor(String name, String author);
    @Override
    Optional<Product> findById(Long id);

    @Override
    void deleteById(Long aLong);
}