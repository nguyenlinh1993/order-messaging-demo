package com.demo.messaging.service;

import com.demo.messaging.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service to simulate inventory updates.
 */
@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    /**
     * Simulate updating inventory for the given order.
     *
     * @param orderEvent the order event to process
     */
    public void updateInventory(OrderEvent orderEvent) {
        log.info("📦 Updating inventory for product: {}, quantity: {}, orderId: {}",
                orderEvent.productId(),
                orderEvent.quantity(),
                orderEvent.orderId());

        // Simulate fast processing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("✅ Inventory updated successfully for product: {}", orderEvent.productId());
    }
}
