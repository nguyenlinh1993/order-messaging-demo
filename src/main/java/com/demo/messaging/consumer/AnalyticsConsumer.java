package com.demo.messaging.consumer;

import com.demo.messaging.config.RabbitMQConfig;
import com.demo.messaging.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer that tracks order analytics.
 *
 * This class demonstrates extensibility — adding a new consumer
 * WITHOUT modifying any existing code (OrderController, Producer, etc.)
 *
 * NOTE: This consumer is disabled by default (no @Component).
 * To enable it during the demo, uncomment @Component and restart.
 */
//@Component  // ← UNCOMMENT THIS DURING DEMO to enable
public class AnalyticsConsumer {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsConsumer.class);

    /**
     * Listen to order queue and track analytics.
     *
     * @param orderEvent the received order event
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleOrderEvent(OrderEvent orderEvent) {
        log.info("📊 [AnalyticsConsumer] Tracking order: orderId={}, product={}, qty={}, user={}",
                orderEvent.orderId(),
                orderEvent.productId(),
                orderEvent.quantity(),
                orderEvent.userEmail());

        // In a real system, you would:
        // - Save to analytics database
        // - Send to data warehouse
        // - Update real-time dashboard
        // - Track conversion metrics

        log.info("✅ [AnalyticsConsumer] Analytics recorded for order: {}", orderEvent.orderId());
    }
}
