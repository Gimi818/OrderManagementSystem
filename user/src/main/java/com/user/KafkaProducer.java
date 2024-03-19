package com.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.dto.UserIdDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;


import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public void sendUserIdMessage(String topic, UserIdDto userIdDto) {

        try {
            String userIdJson = objectMapper.writeValueAsString(userIdDto);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, userIdJson).toCompletableFuture();
            future.thenAccept(result -> log.info("Successfully sent message to topic: {}, userId: {}", topic, userIdDto.id()))
                    .exceptionally(ex -> {
                        log.error("Failed to send message to topic: {}, error: {}", topic, ex.getMessage());
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Could not serialize UserIdDto object to JSON", e);
        }
    }
}
