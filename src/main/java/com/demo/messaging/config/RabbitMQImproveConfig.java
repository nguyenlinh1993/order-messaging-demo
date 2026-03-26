package com.demo.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for messaging infrastructure.
 */
//@Configuration
public class RabbitMQImproveConfig {
    public static final String EMAIL_QUEUE_NAME = "email.queue";
    public static final String INVENTORY_QUEUE_NAME = "inventory.queue";
    public static final String EXCHANGE_NAME = "order.exchange2";

    /**
     * Declare the inventory queue.
     */
//    @Bean("inventoryQueue")
    public Queue inventoryQueue() {
        return QueueBuilder.durable(INVENTORY_QUEUE_NAME).build();
    }

    /**
     * Declare the email queue.
     */
//    @Bean("emailQueue")
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE_NAME).build();
    }

    /**
     * Declare the direct exchange.
     */
//    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Bind the queue to the exchange with the routing key.
     */
//    @Bean
    public Binding bindingEmail(@Qualifier("emailQueue") Queue emailQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(emailQueue)
                .to(orderExchange)
                .with("order.*");
    }

//    @Bean
    public Binding bindingInventory(@Qualifier("inventoryQueue") Queue inventoryQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(inventoryQueue)
                .to(orderExchange)
                .with("order.created");
    }

    /**
     * Configure Jackson message converter for JSON serialization.
     */
//    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configure RabbitTemplate with JSON message converter.
     */
//    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
