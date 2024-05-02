package com.sep.onlinedeliverysystem.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sep.onlinedeliverysystem.domain.dto.OrderDTO;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.OrderService;

@RestController
public class OrderController {

    private OrderService orderService;

    private Mapper<Order, OrderDTO> orderMapper;

    public OrderController(OrderService orderService, Mapper<Order, OrderDTO> orderMapper){
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping(path = "/orders")
    public ResponseEntity<OrderDTO> save(@RequestBody OrderDTO orderDTO){ //Create functionality
        Order orderEntity = orderMapper.mapFrom(orderDTO);
        Order savedOrderEntity = orderService.save(orderEntity); //saves order DTO as entity into our database
        return new ResponseEntity<>(orderMapper.mapTo(savedOrderEntity), HttpStatus.CREATED); //returns our saved entity as a DTO
    }

    @GetMapping(path = "/orders")
    public Page<OrderDTO> listOrders(Pageable pageable){ //Read All functionality
        Page<Order> orders = orderService.findAll(pageable);
        return orders.map(orderMapper::mapTo);
    }

    @GetMapping(path = "/orders/{id}") //Read One functionality
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("id") Long id){
        Optional<Order> foundOrder = orderService.findOne(id);
        return foundOrder.map(orderEntity -> {
            OrderDTO orderDTO = orderMapper.mapTo(orderEntity);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/orders/status/{status}")
    public List<OrderDTO> getOrdersByStatus(@PathVariable("status") String status){
        List<Order> foundOrders = orderService.findAllByStatus(status);
         return foundOrders.stream().map(orderMapper::mapTo).collect(Collectors.toList());
    }

    @PutMapping(path = "/orders/{id}")
    public ResponseEntity<OrderDTO> fullUpdateOrder(@PathVariable("id") Long id, OrderDTO orderDTO){
        if(!orderService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderDTO.setId(id);
        Order orderEntity = orderMapper.mapFrom(orderDTO);
        Order savedOrder = orderService.save(orderEntity);
        return new ResponseEntity<>(orderMapper.mapTo(savedOrder), HttpStatus.OK);
    }

    @PatchMapping(path = "/orders/{id}")
    public ResponseEntity<OrderDTO> partialUpdate(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO){
        if(!orderService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order orderEntity = orderMapper.mapFrom(orderDTO);
        Order updatedOrder = orderService.partialUpdate(id, orderEntity);
        return new ResponseEntity<>(orderMapper.mapTo(updatedOrder), HttpStatus.OK);
    }

    @DeleteMapping(path = "/orders/{id}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long id){
        orderService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
