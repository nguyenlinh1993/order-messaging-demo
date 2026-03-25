package com.demo.messaging.model;

/**
 * Order event model for message passing.
 * Uses Java record for immutability and conciseness.
 */
public record OrderEvent(
    String orderId,
    String userEmail,
    String productId,
    int quantity
) {}
