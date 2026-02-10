package com.balicki.flashsales.orders;

import java.util.UUID;

public record OrderEvent(
        UUID orderId,
        Long userId,
        Integer quantity,
        Long productId
) {}
