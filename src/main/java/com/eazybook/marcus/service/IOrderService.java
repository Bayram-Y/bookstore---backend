package com.eazybook.marcus.service;


import com.eazybook.marcus.dto.OrderRequestDto;
import com.eazybook.marcus.dto.OrderResponseDto;

import java.util.List;

public interface IOrderService {
    void createOrder(OrderRequestDto orderRequest);

    List<OrderResponseDto> getCustomOrders();

    List<OrderResponseDto> getAllPendingOrders();

    void updateOrderStatus(Long orderId, String orderStatus);
}
