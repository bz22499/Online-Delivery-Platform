package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.*;
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
public class BasketItemRepositoryIT {
    private VendorRepository vendorRepository;
    private MenuItemRepository menuItemRepository;
    private OrderRepository orderRepository;
    private BasketRepository basketRepository;
    private BasketItemRepository basketItemRepository;

    @Autowired
    public BasketItemRepositoryIT(BasketItemRepository basketItemRepository, BasketRepository basketRepository, OrderRepository orderRepository, VendorRepository vendorRepository, MenuItemRepository menuItemRepository){
        this.vendorRepository = vendorRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.basketRepository = basketRepository;
        this.basketItemRepository = basketItemRepository;
    }

    @Test
    public void testSingleBasketItemCreationAndFind(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem menuItem = TestUtil.menuItemBuilder1(vendor);
        menuItemRepository.save(menuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(basket, menuItem, 1);
        basketItemRepository.save(basketItem);
        Optional<BasketItem> result = basketItemRepository.findById(basket.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(basketItem);
    }

    @Test
    public void testMultipleBasketItemCreationAndFind(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem menuItem1 = TestUtil.menuItemBuilder1(vendor);
        MenuItem menuItem2 = TestUtil.menuItemBuilder2(vendor);
        MenuItem menuItem3 = TestUtil.menuItemBuilder3(vendor);
        menuItemRepository.save(menuItem1);
        menuItemRepository.save(menuItem2);
        menuItemRepository.save(menuItem3);
        BasketItem basketItem1 = TestUtil.basketItemBuilder(basket, menuItem1, 1);
        BasketItem basketItem2 = TestUtil.basketItemBuilder(basket, menuItem1, 2);
        BasketItem basketItem3 = TestUtil.basketItemBuilder(basket, menuItem1, 3);
        basketItemRepository.save(basketItem1);
        basketItemRepository.save(basketItem2);
        basketItemRepository.save(basketItem3);
        Iterable<BasketItem> result = basketItemRepository.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(basketItem1, basketItem2, basketItem3);
    }

    @Test
    public void testBasketItemUpdate(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem menuItem1 = TestUtil.menuItemBuilder1(vendor);
        MenuItem menuItem2 = TestUtil.menuItemBuilder2(vendor);
        menuItemRepository.save(menuItem1);
        menuItemRepository.save(menuItem2);
        BasketItem basketItem = TestUtil.basketItemBuilder(basket, menuItem1, 1);
        basketItemRepository.save(basketItem);
        basketItem.setMenuItem(menuItem2);
        basketItem.setQuantity(2);
        basketItemRepository.save(basketItem);
        Optional<BasketItem> result = basketItemRepository.findById(basket.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(basketItem);
    }

    @Test
    public void deleteBasketItem(){
        Order order = TestUtil.orderBuilder();
        orderRepository.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketRepository.save(basket);
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem menuItem = TestUtil.menuItemBuilder1(vendor);
        menuItemRepository.save(menuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(basket, menuItem, 1);
        basketItemRepository.save(basketItem);
        basketItemRepository.delete(basketItem);
        Optional<BasketItem> result = basketItemRepository.findById(basket.getId());
        assertThat(result).isEmpty();
    }
    
}
