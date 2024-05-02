package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasketRepositoryIntegrationTests {
    private OrderRepository orderRepository;
    private BasketRepository basketRepository;

    @Autowired
    public BasketRepositoryIntegrationTests(BasketRepository basketRepository, OrderRepository orderRepository){
        this.orderRepository = orderRepository;
        this.basketRepository = basketRepository;
    }

    @Test
    public void testSingleBasketCreationAndFind(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        Optional<Basket> result = basketRepository.findById(basket.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(basket);
    }

    @Test
    public void testMultipleBasketCreationAndFind(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket1 = TestUtil.basketBuilder(order);
        Basket basket2 = TestUtil.basketBuilder(order);
        Basket basket3 = TestUtil.basketBuilder(order);
        basketRepository.save(basket1);
        basketRepository.save(basket2);
        basketRepository.save(basket3);
        Iterable<Basket> result = basketRepository.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(basket1, basket2, basket3);
    }

    @Test
    public void testBasketUpdate(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        basket.setId(10000L);
        basketRepository.save(basket);
        Optional<Basket> result = basketRepository.findById(basket.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(basket);

    }

    @Test
    public void testMenuItemDelete(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        basketRepository.delete(basket);
        Optional<Basket> result = basketRepository.findById(basket.getId());
        assertThat(result).isEmpty();
    }
}
