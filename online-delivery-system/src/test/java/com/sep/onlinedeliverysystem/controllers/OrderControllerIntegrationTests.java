package com.sep.onlinedeliverysystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.services.OrderService;
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
public class OrderControllerIntegrationTests {
    private OrderService orderService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public OrderControllerIntegrationTests(OrderService orderService, MockMvc mockMvc){
        this.orderService = orderService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void orderCreateReturns201Created() throws Exception {
        Order order = TestUtil.orderBuilder();
        String orderJson = objectMapper.writeValueAsString(order);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void orderCreateReturnsSavedOrder() throws Exception {
        Order order = TestUtil.orderBuilder();
        String orderJson = objectMapper.writeValueAsString(order);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void listOrdersReturns200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listOrdersReturnsOrders() throws Exception {
        Order order1 = TestUtil.orderBuilder();
        Order order2 = TestUtil.orderBuilder();
        Order order3 = TestUtil.orderBuilder();

        orderService.save(order1);
        orderService.save(order2);
        orderService.save(order3);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3))); // Ensure the response contains 3 orders
    }


    @Test
    public void getOrderReturns200OkWhenOrderExists() throws Exception {
        Order order = TestUtil.orderBuilder();
        Order savedOrder = orderService.save(order);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + savedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getOrderReturns404NotFoundWhenOrderNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getOrderReturnsOrderWhenExists() throws Exception {
        Order order = TestUtil.orderBuilder();
        orderService.save(order);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void fullUpdateOrderReturns404NotFoundWhenOrderNotExists() throws Exception {
        Order order = TestUtil.orderBuilder();
        String orderJson = objectMapper.writeValueAsString(order);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/orders/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void fullUpdateOrderReturns200OkWhenOrderExists() throws Exception {
        Order order = TestUtil.orderBuilder();
        Order savedOrder = orderService.save(order);
        String orderJson = objectMapper.writeValueAsString(order);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/orders/" + savedOrder.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    //NO UPDATE FUNCTIONALITY NEEDED, SKIP FULL UPDATE

    @Test
    public void deleteOrderReturns204WhenOrderNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteOrderReturns204WhenOrderExists() throws Exception {
        Order order = TestUtil.orderBuilder();
        Order savedOrder = orderService.save(order);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/" + savedOrder.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteOrderDeletesOrder() throws Exception {
        Order order = TestUtil.orderBuilder();
        Order savedOrder = orderService.save(order);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/" + savedOrder.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + savedOrder.getId())
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}





















