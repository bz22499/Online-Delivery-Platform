package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.domain.entities.BasketItem;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.repositories.OrderRepository;
import com.sep.onlinedeliverysystem.services.BasketItemService;
import com.sep.onlinedeliverysystem.services.BasketService;
import com.sep.onlinedeliverysystem.services.OrderService;
import org.aspectj.weaver.ast.Or;
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
    private BasketItemService basketItemService;
    private BasketService basketService;

    public OrderServiceImpl(OrderRepository orderRepository, BasketService basketService, BasketItemService basketItemService) {
        this.orderRepository = orderRepository;
        this.basketService = basketService;
        this.basketItemService = basketItemService;
    }

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
        orderEntity.setId(id);
        return orderRepository.findById(id).map(existingOrder -> orderRepository.save(existingOrder)).orElseThrow(() -> new RuntimeException("Order doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAllByStatusIsNull() {return orderRepository.findAllByStatusIsNull(); }

    @Override
    public List<Order> findAllByStatus(String status) { return orderRepository.findAllByStatus(status); }

    @Override
    public void deleteOrderAndDependencies(Order order) {
        List<Basket> baskets = basketService.findByOrder(order.getId());
        for (Basket basket: baskets){
            List<BasketItem> basketItems = basketItemService.findBasketItemByBasket_Id(basket.getId());
            for (BasketItem basketItem : basketItems) {
                basketItemService.delete(basketItem.getId());
            }
        }
    }
}
