package com.order;

import com.order.dto.CartItem;
import com.order.dto.PreparedCartToOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void createOrder(PreparedCartToOrder order) {
        Set<OrderItem> orderItems = convertCartItemsToOrderItems(order.cart().items());

        Order newOrder = Order.builder()
                .userId(order.cart().id())
                .orderName(generateOrderName())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(order.price())
                .build();
        orderItems.forEach(item -> item.setOrder(newOrder));
        newOrder.setItems(orderItems);

        orderRepository.save(newOrder);

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
