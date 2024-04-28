package com.sep.onlinedeliverysystem.services;

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
public class UserAddressServiceIntegrationTests {

    private BasketService basketService;
    private BasketItemService basketItemService;
    private OrderRepository orderRepository;
    private MenuItemRepository menuItemRepository;
    private VendorService vendorService;

    @Autowired
    public UserAddressServiceIntegrationTests(BasketService basketService, BasketItemService basketItemService, OrderRepository orderRepository, MenuItemRepository menuItemRepository, VendorService vendorService) {
        this.basketService = basketService;
        this.basketItemService = basketItemService;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.vendorService = vendorService;
    }

    @Test
    public void testFindByUserEmail(){

    }
}
