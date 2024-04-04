package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderRepositoryIntegrationTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public OrderRepositoryIntegrationTests(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Test
    public void testSingleOrderCreationAndFind() {
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Optional<Order> result = orderRepository.findById(order.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(order);
    }

    @Test
    public void testMultipleOrderCreationAndFind() {
        Order order1 = TestUtil.orderBuilder();
        Order order2 = TestUtil.orderBuilder();
        Order order3 = TestUtil.orderBuilder();
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        Iterable<Order> result = orderRepository.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(order1, order2, order3);
    }

    @Test
    public void testOrderUpdate() {
        Order order = TestUtil.orderBuilder();
        Order savedOrder = orderRepository.save(order);
        order.setId(2L);
        orderRepository.save(order);
        Optional<Order> result = orderRepository.findById(savedOrder.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(order);
    }

    @Test
    public void testOrderDelete() {
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        orderRepository.delete(order);
        Optional<Order> result = orderRepository.findById(order.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByStatusNull() {
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Iterable<Order> result = orderRepository.findAllByStatusIsNull();
        assertThat(result).containsExactly(order);
    }

    @Test
    public void testFindByStatus() {
        Order order = TestUtil.orderBuilder();
        order.setStatus("PENDING");
        orderRepository.save(order);
        Iterable<Order> result = orderRepository.findAllByStatus("PENDING");
        assertThat(result).containsExactly(order);
    }
}
