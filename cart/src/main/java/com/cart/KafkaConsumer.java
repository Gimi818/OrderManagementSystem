package com.cart;

import com.cart.dto.CartRequestDto;
import com.cart.dto.UserIdDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final CartService service;


    @KafkaListener(topics = "sendUserIdMessage", groupId = "cart-creat-group")
    public void consumeUserIdAndCreateCart(String userIdDtoMessage) {
        log.debug("Received message from Kafka: {}", userIdDtoMessage);
        try {
            UserIdDto userIdDto = objectMapper.readValue(userIdDtoMessage, UserIdDto.class);
            log.info("Deserialized message to UserIdDto with ID: {}", userIdDto.id());
            service.createUserCart(userIdDto);
            log.info("Successfully created cart for user ID: {}", userIdDto.id());
        } catch (JsonProcessingException e) {
            log.error("Error deserializing message to CartRequestDto: {}", e.getMessage());

        }
    }
}
