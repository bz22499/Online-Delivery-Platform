package com.sep.onlinedeliverysystem.controllers;

import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.domain.entities.BasketItem;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.repositories.BasketRepository;
import com.sep.onlinedeliverysystem.repositories.MenuItemRepository;
import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import com.sep.onlinedeliverysystem.repositories.VendorRepository;
import com.sep.onlinedeliverysystem.services.BasketItemService;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BasketItemControllerIntegrationTests {
    @Autowired
    private OrderRepository orderRepository;
    private BasketRepository basketRepository;
    private VendorRepository vendorRepository;
    private MenuItemRepository menuItemRepository;
    private BasketItemService basketItemService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BasketItemControllerIntegrationTests(BasketItemService basketItemService, MockMvc mockMvc, ObjectMapper objectMapper, BasketRepository basketRepository, VendorRepository vendorRepository, MenuItemRepository menuItemRepository) {
        this.basketRepository = basketRepository;
        this.vendorRepository = vendorRepository;
        this.menuItemRepository = menuItemRepository;
        this.basketItemService = basketItemService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void createBasketItemReturns201Created() throws Exception {
        // Create basket (needs order)
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        String basketItemJson = objectMapper.writeValueAsString(basketItem);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/basketItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void createBasketItemReturnsSavedBasketItem() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        String basketItemJson = objectMapper.writeValueAsString(basketItem);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/basketItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1L)
        );
    }

    @Test
    public void listBasketItemsByBasketReturns200Ok() throws Exception {
        // Basket must exists
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basketItems/baskets/" + testBasket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listBasketItemsByBasketReturnsBasketItems() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem1 = TestUtil.menuItemBuilder1(testVendor);
        MenuItem testMenuItem2 = TestUtil.menuItemBuilder2(testVendor);
        MenuItem testMenuItem3 = TestUtil.menuItemBuilder3(testVendor);
        menuItemRepository.save(testMenuItem1);
        menuItemRepository.save(testMenuItem2);
        menuItemRepository.save(testMenuItem3);
        BasketItem basketItem1 = TestUtil.basketItemBuilder(testBasket, testMenuItem1, 1);
        BasketItem basketItem2 = TestUtil.basketItemBuilder(testBasket, testMenuItem2, 2);
        BasketItem basketItem3 = TestUtil.basketItemBuilder(testBasket, testMenuItem3, 3);
        basketItemService.save(basketItem1);
        basketItemService.save(basketItem2);
        basketItemService.save(basketItem3);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basketItems/baskets/" + testBasket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", hasSize(3))
        );
    }

    @Test
    public void getBasketItemReturnsHttp200OkWhenExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        basketItemService.save(basketItem);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getBasketItemReturnsHttps404WhenNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basketItems/1")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getBasketItemReturnsItem() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        basketItemService.save(basketItem);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(basketItem.getId())
        );
    }
    
    @Test
    public void fullUpdateReturns200WhenExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        MenuItem testMenuItem2 = TestUtil.menuItemBuilder2(testVendor);
        menuItemRepository.save(testMenuItem2);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        basketItemService.save(basketItem);
        BasketItem basketItemUpdated = TestUtil.basketItemBuilder(testBasket, testMenuItem2, 1);
        String basketItemUpdatedDTOJson = objectMapper.writeValueAsString(basketItemUpdated);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemUpdatedDTOJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    
    @Test
    public void fullUpdateReturns404WhenNotExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItemUpdated = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        String basketItemUpdatedDTOJson = objectMapper.writeValueAsString(basketItemUpdated);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/basketItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemUpdatedDTOJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
    
    @Test
    public void fullUpdateBasketItem() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem1 = TestUtil.menuItemBuilder1(testVendor);
        MenuItem testMenuItem2 = TestUtil.menuItemBuilder2(testVendor);
        menuItemRepository.save(testMenuItem1);
        menuItemRepository.save(testMenuItem2);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem1, 1);
        basketItemService.save(basketItem);
        BasketItem basketItemUpdated = TestUtil.basketItemBuilder(testBasket, testMenuItem2, 1);
        String basketItemUpdatedDTOJson = objectMapper.writeValueAsString(basketItemUpdated);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemUpdatedDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(basketItem.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.menuItem.id").value(testMenuItem2.getId())
        );
    }

    @Test
    public void partialUpdateReturns200WhenItemExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        basketItemService.save(basketItem);
        BasketItem basketItemUpdated = TestUtil.basketItemBuilder(testBasket, testMenuItem, 2);
        basketItemUpdated.setId(basketItem.getId());
        String basketItemUpdatedDTOJson = objectMapper.writeValueAsString(basketItemUpdated);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemUpdatedDTOJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void partialUpdateReturns404WhenItemNotExist() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItemUpdated = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        String basketItemUpdatedDTOJson = objectMapper.writeValueAsString(basketItemUpdated);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/basketItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketItemUpdatedDTOJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

   @Test
   public void partialUpdateUpdatesItem() throws Exception {
       Order testOrder = TestUtil.orderBuilder();
       orderRepository.save(testOrder); // this generates the id for order object
       Basket testBasket = TestUtil.basketBuilder(testOrder);
       basketRepository.save(testBasket);
       // Create MenuItem (needs vendor)
       Vendor testVendor = TestUtil.vendorBuild1();
       vendorRepository.save(testVendor);
       MenuItem testMenuItem1 = TestUtil.menuItemBuilder1(testVendor);
       MenuItem testMenuItem2 = TestUtil.menuItemBuilder2(testVendor);
       menuItemRepository.save(testMenuItem1);
       menuItemRepository.save(testMenuItem2);
       BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem1, 1);
       basketItemService.save(basketItem);
       BasketItem basketItemUpdated = TestUtil.basketItemBuilder(testBasket, testMenuItem1, 2);
       basketItemUpdated.setId(basketItem.getId());
       String basketItemUpdatedDTOJson = objectMapper.writeValueAsString(basketItemUpdated);
       mockMvc.perform(
               MockMvcRequestBuilders.put("/basketItems/" + basketItem.getId())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(basketItemUpdatedDTOJson)
       ).andExpect(
               MockMvcResultMatchers.jsonPath("$.id").value(basketItem.getId())
       ).andExpect(
               MockMvcResultMatchers.jsonPath("$.quantity").value(basketItemUpdated.getQuantity())
       );
   }

    @Test
    public void deleteBasketItemReturns204WhenItemNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/basketItems/1")
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteBasketItemReturns204WhenItemExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        basketItemService.save(basketItem);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteBasketItemDeletesItem() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketRepository.save(testBasket);
        // Create MenuItem (needs vendor)
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testMenuItem = TestUtil.menuItemBuilder1(testVendor);
        menuItemRepository.save(testMenuItem);
        BasketItem basketItem = TestUtil.basketItemBuilder(testBasket, testMenuItem, 1);
        basketItemService.save(basketItem);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basketItems/" + basketItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
