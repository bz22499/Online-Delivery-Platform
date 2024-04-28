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
public class BasketServiceIntegrationTests {
    private BasketService basketService;
    private BasketItemService basketItemService;
    private OrderService orderService;
    private VendorService vendorService;

    @Autowired
    public BasketServiceIntegrationTests(BasketService basketService, BasketItemService basketItemService, OrderService orderService, VendorService vendorService) {
        this.basketService = basketService;
        this.basketItemService = basketItemService;
        this.orderService = orderService;
        this.vendorService = vendorService;
    }

    @Test
    public void testFindByOrderIdReturnsCorrectOrder() {
        //create order
        Order order = TestUtil.orderBuilder("Pending");
        order = orderService.save(order);

        //add to basket
        Vendor vendor = TestUtil.vendorBuild1();
        MenuItem menuItem = TestUtil.menuItemBuilder1(vendor);
        orderService.findAllByStatus("Pending");

        //retrieve basket by id
        Long basketId = menuItem.getId();
        List basket = basketItemService.findBasketItemByBasket_Id(basketId);

        //test functionality
        assertThat(basket).isNotNull();
        assertThat(basket).isNotEmpty();
        assertThat(basket.getItems().get(0).getMenuItem()).isEqualTo(menuItem);
    }

    @Test
    public void testAddItemToBasket() {
        // create a sample order
        Order order = TestUtil.orderBuilder("Pending");
        order = orderService.save(order);

        // add items to the basket
        Vendor vendor = TestUtil.vendorBuild1();
        MenuItem menuItem = TestUtil.menuItemBuilder1(vendor);
        basketService.addItemToBasket(order.getId(), menuItem.getId(), 2);

        // retrieve the basket
        Basket basket = basketService.getBasketByOrderId(order.getId());

        // test functionality
        assertThat(basket).isNotNull();
        assertThat(basket.getItems()).isNotEmpty();
        assertThat(basket.getItems().get(0).getMenuItem()).isEqualTo(menuItem);
    }

}
