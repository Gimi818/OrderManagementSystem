package com.product.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.dto.AddProductToCartDto;
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



    public void sendProductToCartMessage(String topic, AddProductToCartDto addProductToCartDto) {

        try {
            String productJson = objectMapper.writeValueAsString(addProductToCartDto);
            log.info("Sending product to cart message, topic: {}, productId: {}", topic, addProductToCartDto.productId());
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, productJson).toCompletableFuture();
            future.thenAccept(result -> log.info("Successfully sent message to topic: {}, product id : {}", topic, addProductToCartDto.productId()))
                    .exceptionally(ex -> {
                        log.error("Failed to send message to topic: {}, error: {}", topic, ex.getMessage());
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Could not serialize AddProductToCartDto object to JSON", e);
        }
    }


}
