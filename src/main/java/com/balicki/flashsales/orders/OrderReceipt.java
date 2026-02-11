package com.balicki.flashsales.orders;

import java.util.UUID;

public record OrderReceipt(
        UUID id,
        String status,
        String message
) {
}
