package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Long> {
    List<Basket> findByOrderId(Long orderId);
}
