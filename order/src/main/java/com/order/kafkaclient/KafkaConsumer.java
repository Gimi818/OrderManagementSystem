package com.order.kafkaclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.OrderService;
import com.order.dto.ConfirmationOrderStatus;
import com.order.dto.PreparedCartToOrder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService service;


    @KafkaListener(topics = "order-initiated", groupId = "order-group")
    public void consumeOrder(String message) {
        log.debug("Received message from Kafka: {}", message);
        try {
            PreparedCartToOrder preparedCartToOrder = objectMapper.readValue(message, PreparedCartToOrder.class);
            log.info("Deserialized message to Cart with ID: {}", preparedCartToOrder.cart().id());
            service.createOrder(preparedCartToOrder);
            log.info("Successfully created order for Cart ID: {}", preparedCartToOrder.cart().id());
        } catch (JsonProcessingException e) {
            log.error("Error deserializing message to Cart: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "order-confirmation", groupId = "order-group")
    public void catchOrderConfirmation(String message) {
        log.debug("Received message from Kafka: {}", message);
        try {
            ConfirmationOrderStatus order = objectMapper.readValue(message, ConfirmationOrderStatus.class);
            log.info("Deserialized message to Order with ID: {}", order.statusId());

            service.confirmOrder(order);

            log.info("Successfully created order  ID: {}", order.statusId());
        } catch (JsonProcessingException e) {
            log.error("Error deserializing message to Cart: {}", e.getMessage());
        }
    }

}
