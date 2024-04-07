package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import com.sep.onlinedeliverysystem.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){ this.orderRepository = orderRepository; }

    @Override
    public Order save(Order orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Override
    public List<Order> findAll() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> findOne(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Order partialUpdate(Long id, Order orderEntity) {
        return orderRepository.findById(id).map(existingOrder -> {
            Optional.ofNullable(orderEntity.getStatus()).ifPresent(existingOrder::setStatus);
            Optional.ofNullable(orderEntity.getUserAddress()).ifPresent(existingOrder::setUserAddress);
            return orderRepository.save(existingOrder);
        }).orElseThrow(() -> new RuntimeException("Order doesn't exist"));
    }


    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
