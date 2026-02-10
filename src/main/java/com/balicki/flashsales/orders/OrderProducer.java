package com.balicki.flashsales.orders;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(OrderEvent event) {
        kafkaTemplate.send("flash-sales-orders", event.orderId().toString(), event);

        System.out.println("Event sent to Kafka: " + event);
    }
}
