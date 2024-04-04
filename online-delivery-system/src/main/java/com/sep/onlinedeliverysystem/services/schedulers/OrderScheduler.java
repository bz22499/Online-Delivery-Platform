package com.sep.onlinedeliverysystem.services.schedulers;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.services.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderScheduler {
    private final OrderService orderService;

    public OrderScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "@daily")
    public void scheduledDelete() {
        List<Order> inactiveOrders = orderService.findAllByStatus("PENDING");
        for (Order inactiveOrder : inactiveOrders) {
            orderService.delete(inactiveOrder.getId());
        }
    }
}

