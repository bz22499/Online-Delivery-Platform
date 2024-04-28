package com.sep.onlinedeliverysystem.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.sep.onlinedeliverysystem.domain.entities.*;
import com.sep.onlinedeliverysystem.repositories.MenuItemRepository;
import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import com.sep.onlinedeliverysystem.repositories.UserAddressRepository;

import com.sep.onlinedeliverysystem.repositories.UserRepository;
import com.sep.onlinedeliverysystem.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sep.onlinedeliverysystem.TestUtil;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserAddressServiceIntegrationTests {

    private BasketService basketService;
    private BasketItemService basketItemService;
    private OrderRepository orderRepository;
    private MenuItemRepository menuItemRepository;
    private VendorService vendorService;
    private UserService userService;
    private UserAddressRepository userAddressRepository;
    private UserRepository userRepository;

    @Autowired
    public UserAddressServiceIntegrationTests(BasketService basketService, BasketItemService basketItemService, OrderRepository orderRepository, MenuItemRepository menuItemRepository, VendorService vendorService, UserService userService, UserAddressRepository userAddressRepository, UserRepository userRepository) {
        this.basketService = basketService;
        this.basketItemService = basketItemService;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.vendorService = vendorService;
        this.userService = userService;
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void testFindByUserEmail(){

        User user1 = TestUtil.userBuild1();
        User user2 = TestUtil.userBuild1();
        User user3 = TestUtil.userBuild1();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        UserAddress userAddress1 = TestUtil.userAddressBuild1(user1);
        UserAddress userAddress2 = TestUtil.userAddressBuild1(user2);
        UserAddress userAddress3 = TestUtil.userAddressBuild1(user3);

        userAddressRepository.save(userAddress1);
        userAddressRepository.save(userAddress2);
        userAddressRepository.save(userAddress3);

        Optional<User> userAddressFound = userService.findOne(String.valueOf(userAddress1));

        assertThat(userAddressFound).isNotNull();
    }
}
