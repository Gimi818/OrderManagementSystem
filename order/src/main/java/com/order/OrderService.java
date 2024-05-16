package com.order;

import com.order.dto.*;
import com.order.kafkaclient.KafkaProducer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;

    public void createOrder(PreparedCartToOrder preparedCart) {
        Order newOrder = buildOrder(preparedCart);
        orderRepository.save(newOrder);
        Set<OrderItemDto> items = mapOrderItemsToDto(newOrder.getItems());
        kafkaProducer.verificationOrder("stockVerification", new StockVerificationOrder(newOrder.getId(), items));
    }


    public void confirmOrRejectOrder(ConfirmationOrderStatus confirmationOrderStatus) {
        Order order = orderRepository.findById(confirmationOrderStatus.statusId()).orElseThrow();//dodac exception
        if (confirmationOrderStatus.status()) {
            order.setOrderStatus(OrderStatus.ACCEPTED);
            kafkaProducer.sendConfirmationOrderEmail("ConfirmationOrderEmail", order);
            log.info("Order {} accepted and confirmation email sent.", order.getId());
        } else {
            order.setOrderStatus(OrderStatus.CANCELLED);
            log.info("Order {} cancelled.", order.getId());
        }

        orderRepository.save(order);
    }

    private Order buildOrder(PreparedCartToOrder preparedCart) {
        Set<OrderItem> orderItems = convertCartItemsToOrderItems(preparedCart.cart().items());
        Order newOrder = Order.builder()
                .userId(preparedCart.cart().id())
                .orderName(OrderNameGenerator.generate())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(preparedCart.price())
                .items(orderItems)
                .build();
        orderItems.forEach(item -> item.setOrder(newOrder));
        return newOrder;
    }

    private Set<OrderItem> convertCartItemsToOrderItems(Set<CartItem> cartItems) {
        return cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(cartItem.productName());
            orderItem.setProductId(cartItem.productId());
            orderItem.setQuantity(cartItem.quantity());
            return orderItem;
        }).collect(Collectors.toSet());
    }


//    private OrderEmail toOrderEmail(Order order) {
//        Set<OrderItemDto> items = order.getItems().stream()
//                .map(item -> new OrderItemDto(item.getProductId(), item.getProductName(), item.getQuantity(), item.getPrice()))
//                .collect(Collectors.toSet());
//
//        return new OrderEmail(order.getOrderName(), order.getTotalPrice(), items);
//    }
private Set<OrderItemDto> mapOrderItemsToDto(Set<OrderItem> orderItems) {
    return orderItems.stream()
            .map(item -> new OrderItemDto(item.getProductId(), item.getQuantity()))
            .collect(Collectors.toSet());
}
}
