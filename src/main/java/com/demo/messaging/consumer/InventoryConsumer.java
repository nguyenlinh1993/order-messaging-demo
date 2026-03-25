package com.demo.messaging.consumer;

import com.demo.messaging.config.RabbitMQConfig;
import com.demo.messaging.model.OrderEvent;
import com.demo.messaging.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer that processes order events for inventory updates.
 */
@Component
public class InventoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Listen to order queue and process inventory updates.
     * 
     * @param orderEvent the received order event
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleOrderEvent(OrderEvent orderEvent) {
        log.info("📬 [InventoryConsumer] Received order event: orderId={}", orderEvent.orderId());

        try {
            inventoryService.updateInventory(orderEvent);
        } catch (Exception e) {
            log.error("❌ [InventoryConsumer] Failed to update inventory for order: orderId={}, error={}",
                    orderEvent.orderId(), e.getMessage());
            throw e;
        }
    }
}
