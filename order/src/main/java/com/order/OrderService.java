package com.order;

import com.order.dto.*;
import com.order.exception.exceptions.NotFoundException;
import com.order.kafkaclient.KafkaProducer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.order.OrderService.ErrorMessages.NOT_FOUND_BY_ID;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final OrderMapper orderMapper;

    public void createOrder(PreparedCartToOrder preparedCart) {
        Order newOrder = buildOrder(preparedCart);
        orderRepository.save(newOrder);
        Set<OrderItemDto> items = mapOrderItemsToDto(newOrder.getItems());
        kafkaProducer.verificationOrder("stockVerification", new StockVerificationOrder(newOrder.getId(), items));
    }


    public void handleOrderConfirmation(ConfirmationOrderStatus confirmationOrderStatus) {
        Order order = findOrderById(confirmationOrderStatus.statusId());
        OrderEmail orderEmail = orderMapper.entityToOrderEmail(order);
        processOrderConfirmation(confirmationOrderStatus, order, orderEmail);
        orderRepository.save(order);
    }

     void processOrderConfirmation(ConfirmationOrderStatus confirmationOrderStatus, Order order, OrderEmail orderEmail) {
        if (confirmationOrderStatus.status()) {
            order.setOrderStatus(OrderStatus.ACCEPTED);
            kafkaProducer.sendConfirmationOrderEmail("ConfirmationOrderEmail", orderEmail);
            log.info("Order {} accepted and confirmation email sent.", order.getId());
        } else {
            order.setOrderStatus(OrderStatus.CANCELLED);
            log.info("Order {} cancelled.", order.getId());
        }
    }


    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID));
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


    private Set<OrderItemDto> mapOrderItemsToDto(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> new OrderItemDto(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toSet());
    }

    static final class ErrorMessages {
        static final String NOT_FOUND_BY_ID = "Order with id %d not found";


    }
}
