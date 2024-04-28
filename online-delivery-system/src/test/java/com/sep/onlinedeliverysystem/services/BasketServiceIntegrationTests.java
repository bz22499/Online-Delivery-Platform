package com.sep.onlinedeliverysystem.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.domain.entities.BasketItem;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasketServiceIntegrationTests {

    @Autowired
    private BasketService basketService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public BasketServiceIntegrationTests(BasketService basketService, OrderRepository orderRepository) {
        this.basketService = basketService;
        this.orderRepository = orderRepository;
    }

    @Test
    public void testFindByOrderIdReturnsCorrectOrder() {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket basket1 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket1);
        Basket basket2 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket2);
        Basket basket3 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket3);

        List<Basket> basketsFound = basketService.findByOrder(testOrder.getId());

        assertThat(basketsFound).isNotNull();
        assertThat(basketsFound).hasSize(3);
    }
}
