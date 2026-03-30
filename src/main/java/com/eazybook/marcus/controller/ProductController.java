package com.eazybook.marcus.controller;

import com.eazybook.marcus.dto.ErrorResponseDto;
import com.eazybook.marcus.dto.ProductResponseDto;
import com.eazybook.marcus.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService iProductService;

    // Get All Products
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts() throws InterruptedException { // DTO Pattern
        List<ProductResponseDto> productList = iProductService.getProducts();
        return ResponseEntity.ok().body(productList) ;
    }

    //  GET SINGLE PRODUCT
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        ProductResponseDto product = iProductService.getProduct(id);
        return ResponseEntity.ok(product);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),HttpStatus.SERVICE_UNAVAILABLE,
                exception.getMessage(), LocalDateTime.now());
        return  new ResponseEntity<>(errorResponseDto, HttpStatus.SERVICE_UNAVAILABLE);   // second way
    }
}
