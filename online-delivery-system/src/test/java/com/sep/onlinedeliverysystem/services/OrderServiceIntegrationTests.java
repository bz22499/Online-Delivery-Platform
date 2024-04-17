package com.sep.onlinedeliverysystem.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
public class OrderServiceIntegrationTests {
    private OrderService orderService;
    private BasketService basketService;
    private BasketItemService basketItemService;
    private MenuItemService menuItemService;
    private VendorService vendorService;

    @Autowired
    public OrderServiceIntegrationTests(OrderService orderService, BasketService basketService, BasketItemService basketItemService, MenuItemService menuItemService, VendorService vendorService){
        this.orderService = orderService;
        this.basketService = basketService;
        this.basketItemService = basketItemService;
        this.menuItemService = menuItemService;
        this.vendorService = vendorService;
    }

    @Test
    public void testThatFindAllByStatusNullReturnsCorrectOrders(){
        Order order1 = TestUtil.orderBuilder();
        Order order2 = TestUtil.orderBuilder();
        order2.setStatus("PENDING");
        orderService.save(order1);
        orderService.save(order2);
        List<Order> result = orderService.findAllByStatusIsNull();
        assertThat(result).containsExactly(order1);
    }

    @Test
    public void testThatFindAllByStatusReturnsCorrectOrders(){
        Order order1 = TestUtil.orderBuilder();
        order1.setStatus("PENDING");
        Order order2 = TestUtil.orderBuilder();
        order2.setStatus("COLLECTED");
        orderService.save(order1);
        orderService.save(order2);
        List<Order> result = orderService.findAllByStatus("PENDING");
        assertThat(result).containsExactly(order1);
    }

    @Test
    public void testThatDeleteOrderAndDependenciesDeletesOrdersAndDependencies(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorService.save(vendor);
        MenuItem menuItem = TestUtil.menuItemBuilder1(vendor);
        menuItemService.save(menuItem);
        Order order = TestUtil.orderBuilder();
        orderService.save(order);
        Basket basket = TestUtil.basketBuilder(order);
        basketService.save(basket);
        BasketItem basketItem = TestUtil.basketItemBuilder(basket, menuItem, 1);
        basketItemService.save(basketItem);
        orderService.deleteOrderAndDependencies(order);
        assertThat(orderService.findAll()).isEmpty();
        assertThat(basketService.findAll()).isEmpty();
        assertThat(basketItemService.findBasketItemByBasket_Id(basket.getId())).isEmpty();
    }
}
