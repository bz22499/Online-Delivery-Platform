package com.sep.onlinedeliverysystem.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.sep.onlinedeliverysystem.repositories.MenuItemRepository;
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
public class BasketItemServiceIntegrationTests {

    private BasketService basketService;
    private BasketItemService basketItemService;
    private OrderRepository orderRepository;
    private MenuItemRepository menuItemRepository;
    private VendorService vendorService;

    @Autowired
    public BasketItemServiceIntegrationTests(BasketService basketService, BasketItemService basketItemService, OrderRepository orderRepository, MenuItemRepository menuItemRepository, VendorService vendorService) {
        this.basketService = basketService;
        this.basketItemService = basketItemService;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.vendorService = vendorService;
    }

    @Test
    public void testFindByBasketId() {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);

        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketService.save(testBasket);

        Vendor vendor1 = TestUtil.vendorBuild1();
        Vendor vendor2 = TestUtil.vendorBuild2();
        Vendor vendor3 = TestUtil.vendorBuild3();

        vendorService.save(vendor1);
        vendorService.save(vendor2);
        vendorService.save(vendor3);

        MenuItem menuItem1 = TestUtil.menuItemBuilder1(vendor1);
        MenuItem menuItem2 = TestUtil.menuItemBuilder2(vendor2);
        MenuItem menuItem3 = TestUtil.menuItemBuilder3(vendor3);

        menuItemRepository.save(menuItem1);
        menuItemRepository.save(menuItem2);
        menuItemRepository.save(menuItem3);

        BasketItem item1 = TestUtil.basketItemBuilder(testBasket, menuItem1, 1);
        BasketItem item2 = TestUtil.basketItemBuilder(testBasket, menuItem2, 1);
        BasketItem item3 = TestUtil.basketItemBuilder(testBasket, menuItem3, 1);

        basketItemService.save(item1);
        basketItemService.save(item2);
        basketItemService.save(item3);

        List<BasketItem> basketItemsFound = basketItemService.findBasketItemByBasket_Id(testBasket.getId());

        assertThat(basketItemsFound).isNotNull();
        assertThat(basketItemsFound).hasSize(3);
    }
}
