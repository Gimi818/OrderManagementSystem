package com.product.stockManager;

import com.product.Product;
import com.product.ProductRepository;
import com.product.dto.order.OrderStatus;
import com.product.dto.order.StockVerificationOrder;
import com.product.kafka.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.product.dto.order.OrderItem;


@Service
@AllArgsConstructor
@Log4j2
public class StockManager {
    private final ProductRepository repository;
    private final KafkaProducer kafkaProducer;


    @Transactional
    public void checkInventory(StockVerificationOrder order) {
        Map<Long, Integer> productIdToRequiredQuantity = getOrderProductQuantities(order);
        List<Product> products = repository.findAllById(productIdToRequiredQuantity.keySet());

        if (areAllProductsInStock(products, productIdToRequiredQuantity)) {
            sendOrderConfirmation(order.id(), products, productIdToRequiredQuantity);
        } else {
            kafkaProducer.sendOrderStatus("order-confirmation", new OrderStatus(order.id(), false));
        }
    }

    void sendOrderConfirmation(Long orderId, List<Product> products, Map<Long, Integer> productIdToRequiredQuantity) {
        updateProductInventory(products, productIdToRequiredQuantity);
        kafkaProducer.sendOrderStatus("order-confirmation", new OrderStatus(orderId, true));
        log.info("All products are in stock for the order: {}", orderId);
    }


    boolean areAllProductsInStock(List<Product> products, Map<Long, Integer> productIdToRequiredQuantity) {
        for (Product product : products) {
            int requiredQuantity = productIdToRequiredQuantity.get(product.getId());
            if (product.getStockQuantity() < requiredQuantity) {
                log.info("Insufficient stock for product ID: {}, Required: {}, Available: {}",
                        product.getId(), requiredQuantity, product.getStockQuantity());
                return false;
            }
        }
        return true;
    }


    void updateProductInventory(List<Product> products, Map<Long, Integer> productIdToRequiredQuantity) {
        for (Product product : products) {
            var requiredQuantity = productIdToRequiredQuantity.get(product.getId());
            var updatedQuantity = product.getStockQuantity() - requiredQuantity;
            product.setStockQuantity(updatedQuantity);
            repository.save(product);
        }
    }

    private Map<Long, Integer> getOrderProductQuantities(StockVerificationOrder order) {
        return order.items().stream()
                .collect(Collectors.toMap(OrderItem::productId, OrderItem::quantity));
    }
}


