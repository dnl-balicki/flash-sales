package com.balicki.flashsales.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private static final Logger LOG = LoggerFactory.getLogger(OrderListener.class);

    @KafkaListener(topics = "flash-sales-orders", groupId = "flash-sales-group")
    public void processOrder(OrderEvent event) {
        LOG.info("Received Order: {}", event.orderId());
        LOG.info("User: {}", event.userId());
        LOG.info("Product: {}", event.productId());
        LOG.info("Quantity: {}", event.quantity());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Order Complete: {}", event.orderId());
    }
}
