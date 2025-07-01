package com.ecommerce.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import com.ecommerce.notification.payload.OrderStatus;

@Service
public class OrderEventConsumer {
    @RabbitListener(queues="${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderEvent){
        System.out.println("Received order event"+orderEvent);        

        Long orderId=orderEvent.getOrderId();
        OrderStatus orderStatus = orderEvent.getOrderStatus();

        System.out.println("Order ID: "+orderId);
        System.out.println("Status: "+orderStatus);
    }
}
