package com.balicki.flashsales.orders;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse (
        UUID id,
        Long productId,
        Integer quantity,
        String status,
        LocalDateTime createdAt
) {
}
