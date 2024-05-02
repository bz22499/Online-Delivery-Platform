package com.sep.onlinedeliverysystem.services.schedulers;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.services.OrderService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderSchedulerIntegrationTests {
    private OrderService orderService;

    private OrderScheduler orderScheduler;

    @Autowired
    public OrderSchedulerIntegrationTests(OrderService orderService, OrderScheduler orderScheduler) {
        this.orderService = orderService;
        this.orderScheduler = orderScheduler;
    }

    @Test
    public void testThatSchedulerCorrectlyDeletesOrders(){
        Order order1 = TestUtil.orderBuilder();
        order1.setStatus("PENDING");
        Order order2 = TestUtil.orderBuilder();
        order2.setStatus("DELETE");
        Order order3 = TestUtil.orderBuilder();
        orderService.save(order1);
        orderService.save(order2);
        orderService.save(order3);
        orderScheduler.scheduledDelete();
        List<Order> result = orderService.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatScheduledDeleteOnlyDeletesSpecificOrders(){
        Order order1 = TestUtil.orderBuilder();
        order1.setStatus("PENDING");
        Order order2 = TestUtil.orderBuilder();
        order2.setStatus("DELETE");
        Order order3 = TestUtil.orderBuilder();
        Order order4 = TestUtil.orderBuilder();
        order4.setStatus("COLLECTION");
        orderService.save(order1);
        orderService.save(order2);
        orderService.save(order3);
        orderService.save(order4);
        orderScheduler.scheduledDelete();
        List<Order> result = orderService.findAll();
        assertThat(result).containsExactly(order4);
    }
}