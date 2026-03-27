package com.eazybook.marcus.controller;

import com.eazybook.marcus.constants.ApplicationConstants;
import com.eazybook.marcus.dto.*;
import com.eazybook.marcus.entity.Product;
import com.eazybook.marcus.service.IContactService;
import com.eazybook.marcus.service.IOrderService;
import com.eazybook.marcus.service.IProductService;
import com.eazybook.marcus.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IOrderService iOrderService;
    private final IContactService iContactService;
    private final IProductService iProductService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getAllPendingOrders() {
        System.out.println("GetMapping: getAllPendingOrders");
        return ResponseEntity.ok().body(iOrderService.getAllPendingOrders());
    }

    @PatchMapping("/orders/{orderId}/confirm")
    public ResponseEntity<ResponseDto> confirmOrder(@PathVariable Long orderId) {
        System.out.println("PatchMapping: confirmOrder");
        iOrderService.updateOrderStatus(orderId, ApplicationConstants.ORDER_STATUS_CONFIRMED);
        return ResponseEntity.ok(
                new ResponseDto("200", "Order #" + orderId + " has been approved.")
        );
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public ResponseEntity<ResponseDto> cancelOrder(@PathVariable Long orderId) {
        System.out.println("PatchMapping: cancelOrder");
        iOrderService.updateOrderStatus(orderId, ApplicationConstants.ORDER_STATUS_CANCELLED);
        return ResponseEntity.ok(
                new ResponseDto("200", "Order #" + orderId + " has been cancelled.")
        );
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ContactResponseDto>> getAllOpenMessages() {
        System.out.println("GetMapping: getAllOpenMessages");
        return ResponseEntity.ok(iContactService.getAllOpenMessages());
    }

    @PatchMapping("/messages/{contactId}/close")
    public ResponseEntity<ResponseDto> closeMessage(@PathVariable Long contactId) {
        System.out.println("PatchMapping: closeMessage");
        iContactService.updateMessageStatus(contactId, ApplicationConstants.CLOSED_MESSAGE);
        return ResponseEntity.ok(
                new ResponseDto("200", "Contact #" + contactId + " has been closed.")
        );
    }

    @PostMapping("/add-product")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @ModelAttribute ProductRequestDto dto) {
        System.out.println("PostMapping: addProduct");
        ProductResponseDto response = iProductService.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}