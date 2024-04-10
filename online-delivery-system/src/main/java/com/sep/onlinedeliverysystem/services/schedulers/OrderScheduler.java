package com.sep.onlinedeliverysystem.services.schedulers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.services.BasketItemService;
import com.sep.onlinedeliverysystem.services.BasketService;
import com.sep.onlinedeliverysystem.services.OrderService;

@Service
public class OrderScheduler {
    private final OrderService orderService;

    public OrderScheduler(OrderService orderService, BasketService basketService, BasketItemService basketItemService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "@daily")
    public void scheduledDelete() {
        List<Order> ordersToDelete = new ArrayList<Order>();
        ordersToDelete.addAll(orderService.findAllByStatusIsNull());
        ordersToDelete.addAll(orderService.findAllByStatus("PENDING"));
        ordersToDelete.addAll(orderService.findAllByStatus("DELETE"));
        for (Order order : ordersToDelete) {
            orderService.deleteOrderAndDependencies(order);
        }
    }
}

