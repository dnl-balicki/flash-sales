package com.balicki.flashsales.orders;

public record OrderRequest(
        Long productId,
        Integer quantity
) {
}
