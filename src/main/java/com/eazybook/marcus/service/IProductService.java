package com.eazybook.marcus.service;


import com.eazybook.marcus.dto.ProductRequestDto;
import com.eazybook.marcus.dto.ProductResponseDto;
import com.eazybook.marcus.dto.ProductUpdateRequestDto;

import java.util.List;

public interface IProductService {
    List<ProductResponseDto> getProducts();
    ProductResponseDto getProduct(Long id);
    void deleteProduct(Long id);
    ProductResponseDto addProduct(ProductRequestDto productRequestDto);
    ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto productUpdateRequestDto);
}
