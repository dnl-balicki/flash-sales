package com.balicki.flashsales.orders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer orderProducer;
    private final OrderRepository orderRepository;

    public OrderController(OrderProducer orderProducer, OrderRepository orderRepository) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
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

    @PostMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        return orderRepository.findById(id)
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getProductId(),
                        order.getQuantity(),
                        order.getStatus().name(),
                        order.getCreatedAt()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
