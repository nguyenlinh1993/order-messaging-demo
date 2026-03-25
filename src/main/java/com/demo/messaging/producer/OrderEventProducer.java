package com.demo.messaging.producer;

import com.demo.messaging.config.RabbitMQConfig;
import com.demo.messaging.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Producer responsible for sending order events to RabbitMQ.
 */
@Component
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public OrderEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Send an order event to RabbitMQ.
     *
     * @param orderEvent the order event to send
     */
    public void sendOrderEvent(OrderEvent orderEvent) {
        log.info("📤 Publishing order event: orderId={}, userEmail={}, productId={}, quantity={}",
                orderEvent.orderId(),
                orderEvent.userEmail(),
                orderEvent.productId(),
                orderEvent.quantity());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                orderEvent
        );

        log.info("✅ Order event published successfully: orderId={}", orderEvent.orderId());
    }
}
