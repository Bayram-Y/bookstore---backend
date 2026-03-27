package com.eazybook.marcus.service;


import com.eazybook.marcus.dto.ProductRequestDto;
import com.eazybook.marcus.dto.ProductResponseDto;

import java.util.List;

public interface IProductService {
    List<ProductResponseDto> getProducts();

    void deleteProduct(Long id);
    ProductResponseDto addProduct(ProductRequestDto productRequestDto);
}
