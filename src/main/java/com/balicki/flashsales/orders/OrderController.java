package com.balicki.flashsales.orders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        UUID orderId = UUID.randomUUID();

        OrderEvent event = new OrderEvent(
                orderId,
                request.productId(),
                request.quantity(),
                1L
        );

        orderProducer.sendOrder(event);

        return ResponseEntity.accepted().body("Order placed successfully with id: " + orderId);
    }
}
