package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface BasketService {
    Basket save(Basket basketEntity);
    List<Basket> findAll();
    List<Basket> findByOrder(Long orderId);
    Optional<Basket> findOne(Long id);
    boolean Exists(Long id);
    Basket partialUpdate(Long id, Basket basketEntity);
    void delete(Long id);

}
