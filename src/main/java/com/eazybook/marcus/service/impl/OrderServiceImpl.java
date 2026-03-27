package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.constants.ApplicationConstants;
import com.eazybook.marcus.dto.OrderItemResponseDto;
import com.eazybook.marcus.dto.OrderRequestDto;
import com.eazybook.marcus.dto.OrderResponseDto;
import com.eazybook.marcus.entity.Customer;
import com.eazybook.marcus.entity.Order;
import com.eazybook.marcus.entity.OrderItem;
import com.eazybook.marcus.entity.Product;
import com.eazybook.marcus.exception.ResourceNotFoundException;
import com.eazybook.marcus.repository.OrderRepository;
import com.eazybook.marcus.repository.ProductRepository;
import com.eazybook.marcus.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProfileServiceImpl profileService;

    @Override
    public void createOrder(OrderRequestDto orderRequest) {
        Customer customer = profileService.getAuthenticatedCustomer();
        // Create Order
        Order order = new Order();
        order.setCustomer(customer);
        BeanUtils.copyProperties(orderRequest, order);
        order.setOrderStatus(ApplicationConstants.ORDER_STATUS_CREATED);
        // Map OrderItems
        List<OrderItem> orderItems = orderRequest.items().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID",
                            item.productId().toString()));
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            orderItem.setPrice(item.price());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDto> getCustomOrders() {
        Customer customer = profileService.getAuthenticatedCustomer();
//        List<Order> orders = orderRepository.findByCustomerOrderByCreatedAtDesc(customer); // first way:  SQL (Derived Query)
//        List<Order> orders = orderRepository.findOrderByCustomer(customer); // JPQL way  (Our Own Custom Query)
        List<Order> orders = orderRepository.findOrderByCustomerWithNativeQuery(customer.getCustomerId()); // NativeSQLQuery way
        return orders.stream().map(this::mapToOrderResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getAllPendingOrders() {
//        List<Order> orders = orderRepository.findByOrderStatus(ApplicationConstants.ORDER_STATUS_CREATED);  //  SQL
        List<Order> orders = orderRepository.findOrderByStatusWithNativeQuery(ApplicationConstants.ORDER_STATUS_CREATED);
        return orders.stream().map(this::mapToOrderResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(Long orderId, String orderStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        orderRepository.updateOrderStatus(orderId, orderStatus, email);
    }

    /**
     * Map Order entity to OrderResponseDto
     */
    private OrderResponseDto mapToOrderResponseDTO(Order order) {
        // Map Order Items
        List<OrderItemResponseDto> itemDTOs = order.getOrderItems().stream()
                .map(this::mapToOrderItemResponseDTO)
                .collect(Collectors.toList());
        OrderResponseDto orderResponseDto = new OrderResponseDto(order.getOrderId()
                , order.getOrderStatus(), order.getTotalPrice(), order.getCreatedAt().toString()
                , itemDTOs);
        return orderResponseDto;
    }


    /**
     * Map OrderItem entity to OrderItemResponseDto
     */
    private OrderItemResponseDto mapToOrderItemResponseDTO(OrderItem orderItem) {
        OrderItemResponseDto itemDTO = new OrderItemResponseDto(
                orderItem.getProduct().getName(), orderItem.getQuantity(),
                orderItem.getPrice(), orderItem.getProduct().getImageUrl());
        return itemDTO;
    }
}