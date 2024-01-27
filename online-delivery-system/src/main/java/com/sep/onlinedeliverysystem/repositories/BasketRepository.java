package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketRepository extends CrudRepository<Basket, Long> {
    List<Basket> findByOrderId(Long orderId);
}
