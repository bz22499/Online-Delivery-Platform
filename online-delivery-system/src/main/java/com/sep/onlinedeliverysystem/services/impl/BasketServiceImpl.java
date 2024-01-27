package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.repositories.BasketRepository;
import com.sep.onlinedeliverysystem.services.BasketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BasketServiceImpl implements BasketService {
    private BasketRepository basketRepository;

    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public Basket save(Basket basketEntity) {
        return basketRepository.save(basketEntity);
    }

    @Override
    public List<Basket> findAll() {
        return StreamSupport.stream(basketRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<Basket> findByOrder(Long orderId) {
        return StreamSupport.stream(basketRepository.findByOrderId(orderId).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<Basket> findOne(Long id) {
        return basketRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return basketRepository.existsById(id);
    }

    @Override
    public Basket partialUpdate(Long id, Basket basketEntity) {
        basketEntity.setId(id);

        return basketRepository.findById(id).map(existingBasket ->{
            Optional.ofNullable(basketEntity.getOrder()).ifPresent(existingBasket::setOrder);
            return basketRepository.save(existingBasket);
        }).orElseThrow(() -> new RuntimeException("Basket not found"));
    }

    @Override
    public void delete(Long id) {
        basketRepository.deleteById(id);
    }
}
