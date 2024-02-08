package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.BasketItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface BasketItemService {
    BasketItem save(BasketItem basketItemEntity);
    List<BasketItem> findBasketItemByBasket_Id(Long basketId);
    Optional<BasketItem> findOne(Long id);
    boolean Exists(Long id);
    BasketItem partialUpdate(Long id, BasketItem basketEntity);
    void delete(Long id);
    void deleteByMenuItemId(Long id);
}
