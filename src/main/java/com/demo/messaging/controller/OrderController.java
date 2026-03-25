package com.demo.messaging.controller;

import com.demo.messaging.model.OrderEvent;
import com.demo.messaging.producer.OrderEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * REST controller for order operations.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderEventProducer orderEventProducer;

    public OrderController(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    /**
     * Create a new order and publish event to RabbitMQ.
     *
     * @param request the order request containing userEmail, productId, and quantity
     * @return response with order status and orderId
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody OrderRequest request) {
        String orderId = UUID.randomUUID().toString();

        log.info("📥 Received order request: orderId={}, userEmail={}, productId={}, quantity={}",
                orderId, request.userEmail(), request.productId(), request.quantity());

        OrderEvent orderEvent = new OrderEvent(
                orderId,
                request.userEmail(),
                request.productId(),
                request.quantity()
        );

        // Publish event to RabbitMQ (async - do not wait for processing)
        orderEventProducer.sendOrderEvent(orderEvent);

        log.info("✅ Order accepted: orderId={}", orderId);

        return ResponseEntity.ok(Map.of(
                "status", "Order accepted",
                "orderId", orderId
        ));
    }

    /**
     * Request record for creating an order.
     */
    public record OrderRequest(
            String userEmail,
            String productId,
            int quantity
    ) {}
}
