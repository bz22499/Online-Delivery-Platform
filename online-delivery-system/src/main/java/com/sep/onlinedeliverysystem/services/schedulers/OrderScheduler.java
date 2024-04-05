package com.sep.onlinedeliverysystem.services.schedulers;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.services.BasketItemService;
import com.sep.onlinedeliverysystem.services.BasketService;
import com.sep.onlinedeliverysystem.services.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderScheduler {
    private final OrderService orderService;

    public OrderScheduler(OrderService orderService, BasketService basketService, BasketItemService basketItemService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "@daily")
    public void scheduledDelete() {
        List<Order> inactiveOrders = orderService.findAllByStatus("PENDING");
        for (Order inactiveOrder : inactiveOrders) {
            orderService.deleteOrderAndDependencies(inactiveOrder);
        }
    }
}

