package com.balicki.flashsales.orders;

import com.balicki.flashsales.products.Product;
import com.balicki.flashsales.products.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderListener {

    private static final Logger log = LoggerFactory.getLogger(OrderListener.class);

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderListener(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "flash-sales-orders", groupId = "flash-sales-group")
    @Transactional
    public void processOrder(OrderEvent event) {
        log.info("Received Order Event: {}", event.orderId());

        Order order = new Order(
                event.orderId(),
                event.productId(),
                event.userId(),
                event.quantity()
        );

        Optional<Product> productOpt = productRepository.findById(event.productId());

        if (productOpt.isEmpty()) {
            log.error("Product Not Found: {}", event.productId());
            order.setStatus(Order.Status.CANCELLED);
            orderRepository.save(order);
            return;
        }

        Product product = productOpt.get();

        if (product.getStock() < event.quantity()) {
            log.warn("Stock Not enough For Product {}. Order: {}, Stock: {}",
                    product.getName(), event.quantity(), product.getStock());
            order.setStatus(Order.Status.CANCELLED);
            orderRepository.save(order);
            return;
        }

        product.setStock(product.getStock() - event.quantity());

        order.setStatus(Order.Status.COMPLETED);
        orderRepository.save(order);

        log.info("Order Complete: {}. New Stock of {}: {}",
                event.orderId(), product.getName(), product.getStock());
    }
}
