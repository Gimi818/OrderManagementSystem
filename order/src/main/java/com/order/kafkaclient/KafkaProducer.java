package com.order.kafkaclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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


//    public void initiateOrder(String topic, Cart cart) {
//
//        try {
//            String orderJson = objectMapper.writeValueAsString(cart);
//            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, orderJson).toCompletableFuture();
//            future.thenAccept(result -> log.info("Successfully sent message to topic: {}, cartId: {}", topic, cart.getId()))
//                    .exceptionally(ex -> {
//                        log.error("Failed to send message to topic: {}, error: {}", topic, ex.getMessage());
//                        return null;
//                    });
//        } catch (JsonProcessingException e) {
//            log.error("Could not serialize cart object to JSON", e);
//        }
//    }
}
