package com.sep.onlinedeliverysystem.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sep.onlinedeliverysystem.domain.entities.BasketItem;

@Repository
public interface BasketItemRepository extends CrudRepository<BasketItem, Long> {
    List<BasketItem> findByBasketId(Long basketId);
}
