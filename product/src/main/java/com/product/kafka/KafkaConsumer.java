package com.product.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.stockManager.StockManager;
import com.product.dto.order.StockVerificationOrder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final StockManager stockManager;


    @KafkaListener(topics = "stockVerification",groupId = "order-group")
    public void consumeOrder(String message) {
        log.debug("Received message from Kafka: {}", message);
        try {
            StockVerificationOrder stockVerificationOrder = objectMapper.readValue(message, StockVerificationOrder.class);
            log.info("Deserialized message to stockVerificationOrder with ID: {}", stockVerificationOrder.id());
            stockManager.checkInventory(stockVerificationOrder);
            log.info("Successfully created order for stockVerificationOrder ID: {}", stockVerificationOrder.id());
        } catch (JsonProcessingException e) {
            log.error("Error deserializing message to stockVerificationOrder: {}", e.getMessage());
        }
    }

}
