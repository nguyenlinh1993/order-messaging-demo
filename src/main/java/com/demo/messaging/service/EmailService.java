package com.demo.messaging.service;

import com.demo.messaging.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service to simulate email sending.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    /**
     * Simulate sending an email for the given order.
     *
     * @param orderEvent the order event to process
     * @throws InterruptedException if the thread is interrupted during sleep
     */
    public void sendOrderConfirmationEmail(OrderEvent orderEvent) throws InterruptedException {
        log.info("📧 Sending email to user: {} for order: {}", orderEvent.userEmail(), orderEvent.orderId());

        // Simulate processing delay (2-3 seconds)
        long delay = 2000 + (long) (Math.random() * 1000);
        Thread.sleep(delay);

        log.info("✅ Email sent successfully to: {} (took {}ms)", orderEvent.userEmail(), delay);
    }
}
