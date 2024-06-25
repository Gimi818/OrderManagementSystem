package com.emailsender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final OrderConfirmationEmail orderConfirmationEmail;

    @KafkaListener(topics = "ConfirmationOrderEmail", groupId = "emailSender-group-confirmationOrder")
    public void listenForEmailWithTicket(String emailWithTicketJson) {
        try {
            OrderDto order = objectMapper.readValue(emailWithTicketJson, OrderDto.class);
            orderConfirmationEmail.sendEmail(order);
        } catch (JsonProcessingException | MessagingException e) {
            e.printStackTrace();
        }
    }


}
