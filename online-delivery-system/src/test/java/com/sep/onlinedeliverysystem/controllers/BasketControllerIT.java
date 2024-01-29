package com.sep.onlinedeliverysystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import com.sep.onlinedeliverysystem.services.BasketService;
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
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BasketControllerIT {
    @Autowired
    private OrderRepository orderRepository;
    private BasketService basketService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BasketControllerIT(MockMvc mockMvc, BasketService basketService) {
        this.mockMvc = mockMvc;
        this.basketService = basketService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void createBasketReturnsHttp201Created() throws Exception {
        // Create Basket (needs order)
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        String basketJson = objectMapper.writeValueAsString(testBasket);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void createBasketReturnsSavedBasket() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        Order savedOrder = orderRepository.save(testOrder);
        Basket testBasket = TestUtil.basketBuilder(savedOrder);
        String basketJson = objectMapper.writeValueAsString(testBasket);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1L)
        );
    }

    @Test
    public void listBasketsReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void listBasketsReturnsBaskets() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket basket1 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket1);
        Basket basket2 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket2);
        Basket basket3 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket3);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", hasSize(3))
        );
    }

    @Test
    public void listBasketsByOrderReturnsHttp200Ok() throws Exception {
        // Order must exist
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder); // this generates the id for order object
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets/orders/" + testOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void listBasketsByOrderReturnsBaskets() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket basket1 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket1);
        Basket basket2 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket2);
        Basket basket3 = TestUtil.basketBuilder(testOrder);
        basketService.save(basket3);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets/orders/" + testOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", hasSize(3))
        );
    }

    @Test
    public void getBasketReturns200OkWhenExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketService.save(testBasket);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets/" + testBasket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getBasketReturns4004WhenNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets/0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

   @Test
    public void getBasketReturnsBasketWhenExists() throws Exception {
       Order testOrder = TestUtil.orderBuilder();
       orderRepository.save(testOrder);
       Basket testBasket = TestUtil.basketBuilder(testOrder);
       basketService.save(testBasket);
       mockMvc.perform(
               MockMvcRequestBuilders.get("/baskets/" + testBasket.getId())
                       .contentType(MediaType.APPLICATION_JSON)
       )
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBasket.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.order").value(testBasket.getOrder()));
   }

   // DON'T REALLY NEED UPDATE FUNCTIONALITY AT THIS POINT, SKIPPING TO DELETE

    @Test
    public void deleteBasketReturnsHttp204WhenBasketNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/baskets/0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect((MockMvcResultMatchers.status().isNoContent()));
    }

    @Test
    public void deleteBasketReturns204WhenBasketExists() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketService.save(testBasket);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/baskets/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteBasketDeletesBasket() throws Exception {
        Order testOrder = TestUtil.orderBuilder();
        orderRepository.save(testOrder);
        Basket testBasket = TestUtil.basketBuilder(testOrder);
        basketService.save(testBasket);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/baskets/" + testBasket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/baskets/" + testBasket.getId())
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
