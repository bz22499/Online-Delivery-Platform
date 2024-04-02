package com.sep.onlinedeliverysystem.services.schedulers;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import com.sep.onlinedeliverysystem.services.OrderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderScheduler {
    private OrderService orderService;

    public OrderScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledDelete() {
        List<Order> inactiveOrders = orderService.findAllByStatusIsNull();
        for (int i = 0; i < inactiveOrders.size(); i++) {
            orderService.delete(inactiveOrders.get(i).getId());
        }
    }
}

