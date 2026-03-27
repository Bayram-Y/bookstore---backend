
package com.eazybook.marcus.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDto(Long orderId, String status,
                               BigDecimal totalPrice, String createdAt,
                               List<OrderItemResponseDto> items) {
}
