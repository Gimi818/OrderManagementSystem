package com.order.kafkaclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.Order;
import com.order.dto.OrderEmail;
import com.order.dto.StockVerificationOrder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public void verificationOrder(String topic, StockVerificationOrder order) {
        sendMessage(topic, order);
    }

    public void sendConfirmationOrderEmail(String topic, OrderEmail order) {
        sendMessage(topic, order);
    }

    private void sendMessage(String topic, Object message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, messageJson).toCompletableFuture();

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully sent message to topic: {}, message: {}", topic, message);
                } else {
                    log.error("Failed to send message to topic: {}, error: {}", topic, ex.getMessage());
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Could not serialize message object to JSON", e);
        }
    }


}
