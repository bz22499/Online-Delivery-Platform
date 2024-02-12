package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.BasketItem;
import com.sep.onlinedeliverysystem.repositories.BasketItemRepository;
import com.sep.onlinedeliverysystem.services.BasketItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BasketItemServiceImpl implements BasketItemService {
    private BasketItemRepository basketItemRepository;

    public BasketItemServiceImpl(BasketItemRepository basketItemRepository) {
        this.basketItemRepository = basketItemRepository;
    }
    @Override
    public BasketItem save(BasketItem basketItemEntity) {
        return basketItemRepository.save(basketItemEntity);
    }

    @Override
    public List<BasketItem> findBasketItemByBasket_Id(Long basketId) {
        return StreamSupport.stream(basketItemRepository.findByBasketId(basketId).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<BasketItem> findOne(Long id) {
        return basketItemRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return basketItemRepository.existsById(id);
    }

    @Override
    public BasketItem partialUpdate(Long id, BasketItem basketEntity) {
        basketEntity.setId(id);

        return basketItemRepository.findById(id).map(existingBasketItem ->{
            Optional.ofNullable(basketEntity.getBasket()).ifPresent((existingBasketItem::setBasket));
            Optional.ofNullable(basketEntity.getMenuItem()).ifPresent((existingBasketItem::setMenuItem));
            Optional.ofNullable(basketEntity.getQuantity()).ifPresent(existingBasketItem::setQuantity);
            return basketItemRepository.save(existingBasketItem);
        }).orElseThrow(() -> new RuntimeException("Basket item doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        basketItemRepository.deleteById(id);
    }

}
