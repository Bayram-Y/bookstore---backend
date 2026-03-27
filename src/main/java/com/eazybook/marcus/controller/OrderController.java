package com.eazybook.marcus.controller;

import com.eazybook.marcus.dto.OrderRequestDto;
import com.eazybook.marcus.dto.OrderResponseDto;
import com.eazybook.marcus.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService iOrderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto requestDto) {
        iOrderService.createOrder(requestDto);
        return ResponseEntity.ok("Order created successfully!");
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponseDto>> loadCustomeOrders() {
        return ResponseEntity.ok(iOrderService.getCustomOrders());
    }
}
