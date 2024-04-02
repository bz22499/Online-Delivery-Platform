package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order save(Order orderEntity);

    List<Order> findAll();

    Page<Order> findAll(Pageable pageable);

    Optional<Order> findOne(Long id);

    boolean Exists(Long id);

    Order partialUpdate(Long id, Order orderEntity);

    void delete(Long id);

    List<Order> findAllByStatusIsNull();
}
