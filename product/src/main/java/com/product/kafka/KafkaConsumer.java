package com.product.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final ProductService productService;


//    @KafkaListener(topics = "order-initiated")
//    public void onOrderInitiated(String userIdDtoMessage) {
//
//        boolean productsAvailable = productService.checkProductsAvailability(orderDetails);
//
//    }




}
