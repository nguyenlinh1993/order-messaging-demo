package com.demo.messaging.consumer;

import com.demo.messaging.config.RabbitMQConfig;
import com.demo.messaging.model.OrderEvent;
import com.demo.messaging.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer that processes order events for email notifications.
 */
@Component
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Listen to order queue and process email notifications.
     * 
     * @param orderEvent the received order event
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleOrderEvent(OrderEvent orderEvent) {
        log.info("📬 [EmailConsumer] Received order event: orderId={}", orderEvent.orderId());

        try {
            // Simulate random failure (30% chance)
            if (Math.random() < 0.3) {
                throw new RuntimeException("Simulated email failure");
            }

            emailService.sendOrderConfirmationEmail(orderEvent);

        } catch (RuntimeException e) {
            log.error("❌ [EmailConsumer] Failed to process order: orderId={}, error={}",
                    orderEvent.orderId(), e.getMessage());
            throw e; // Re-throw to trigger retry
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("❌ [EmailConsumer] Processing interrupted for order: orderId={}", orderEvent.orderId());
            throw new RuntimeException("Email processing interrupted", e);
        }
    }
}
