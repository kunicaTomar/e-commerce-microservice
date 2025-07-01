package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public Optional<OrderResponse> createOrder(String userId) {
        // 1. Fetch cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        // 2. Calculate total
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Build Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalAmount);

        // 4. Map CartItem → OrderItem
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> {
                    OrderItem oi = new OrderItem();
                    oi.setProductId(item.getProductId());
                    oi.setQuantity(item.getQuantity());
                    oi.setPrice(item.getPrice());
                    oi.setOrder(order);
                    return oi;
                })
                .toList();
        order.setItems(orderItems);

        // 5. Persist
        Order saved = orderRepository.save(order);

        // 6. Clear cart
        OrderCreatedEvent event = new OrderCreatedEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getStatus(),
                mapTOrderItemDTOs(saved.getItems()),
                saved.getTotalAmount(),
                saved.getCreatedAt());
        cartService.clearCart(userId);
        rabbitTemplate.convertAndSend("order.exchange", "order.tracking",
                Map.of("orderId", saved.getId(), "status", "CREATED"));

        // 7. Map to DTO
        return Optional.of(mapToResponse(saved));
    }

    private List<OrderItemDTO> mapTOrderItemDTOs(List<OrderItem> items) {
        return items.stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))))
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(oi -> new OrderItemDTO(
                        oi.getId(),
                        oi.getProductId(),
                        oi.getQuantity(),
                        oi.getPrice(),
                        oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity()))))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                itemDTOs,
                order.getCreatedAt());
    }
}
