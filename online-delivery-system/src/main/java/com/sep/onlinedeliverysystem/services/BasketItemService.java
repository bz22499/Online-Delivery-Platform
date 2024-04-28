package com.sep.onlinedeliverysystem.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.sep.onlinedeliverysystem.domain.entities.BasketItem;

@Component
public interface BasketItemService {
    BasketItem save(BasketItem basketItemEntity);
    List<BasketItem> findBasketItemByBasket_Id(Long basketId);
    Optional<BasketItem> findOne(Long id);
    boolean Exists(Long id);
    BasketItem partialUpdate(BasketItem basketEntity);
    void delete(Long id);
}
