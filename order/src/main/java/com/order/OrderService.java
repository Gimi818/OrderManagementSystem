package com.order;

import com.order.dto.*;
import com.order.kafkaclient.KafkaProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;

    public void createOrder(PreparedCartToOrder preparedCart) {
        Order newOrder = buildOrder(preparedCart);
        orderRepository.save(newOrder);
        Set<OrderItemDto> items = mapOrderItemsToDto(newOrder.getItems());
        kafkaProducer.verificationOrder("stockVerification", new StockVerificationOrder(newOrder.getId(), items));
    }


    private Set<OrderItemDto> mapOrderItemsToDto(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> new OrderItemDto(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toSet());
    }

    public void confirmOrder(ConfirmationOrderStatus confirmationOrderStatus) {
        Order order = orderRepository.findById(confirmationOrderStatus.statusId()).orElseThrow();
        if (confirmationOrderStatus.status())
            order.setOrderStatus(OrderStatus.ACCEPTED);
        else {
            order.setOrderStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
    }

    private Order buildOrder(PreparedCartToOrder preparedCart) {
        Set<OrderItem> orderItems = convertCartItemsToOrderItems(preparedCart.cart().items());
        Order newOrder = Order.builder()
                .userId(preparedCart.cart().id())
                .orderName(generateOrderName())
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
            orderItem.setProductId(cartItem.productId());
            orderItem.setQuantity(cartItem.quantity());
            return orderItem;
        }).collect(Collectors.toSet());
    }

    public static String generateOrderName() {
        Random random = new Random();
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder name = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            name.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return name.toString();
    }
}
