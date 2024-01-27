package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.dto.BasketDTO;
import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BasketController {
    private BasketService basketService;
    private Mapper<Basket, BasketDTO> basketMapper;

    public BasketController(BasketService basketService, Mapper<Basket, BasketDTO> basketMapper) {
        this.basketService = basketService;
        this.basketMapper = basketMapper;
    }

    @PostMapping(path = "/baskets")
    public ResponseEntity<BasketDTO> save(BasketDTO basketDTO) {
        Basket basketEntity = basketMapper.mapFrom(basketDTO);
        Basket savedBasket = basketService.save(basketEntity);
        return new ResponseEntity<>(basketMapper.mapTo(savedBasket), HttpStatus.CREATED);
    }

    @GetMapping(path = "/baskets")
    public List<BasketDTO> listBaskets(){
        List<Basket> baskets = basketService.findAll();
        return baskets.stream().map(basketMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/baskets/{id}")
    public ResponseEntity<BasketDTO> getBasket(@PathVariable("id") Long id) {
        Optional<Basket> foundBasket = basketService.findOne(id);
        return foundBasket.map(basketEntity -> {
            BasketDTO basketDTO = basketMapper.mapTo(basketEntity);
            return new ResponseEntity<>(basketDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/baskets/order/{orderId}")
    public List<BasketDTO> listBasketsByOrder(@PathVariable("orderId") String stringId) {
        Long id = Long.parseLong(stringId);
        List<Basket> baskets = basketService.findByOrder(id);
        return baskets.stream().map(basketMapper::mapTo).collect(Collectors.toList());
    }

    @PutMapping(path = "/baskets/{id}")
    public ResponseEntity<BasketDTO> fullUpdateBasket(@PathVariable("id") Long id, @RequestBody BasketDTO basketDTO) {
        if (!basketService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        basketDTO.setId(id);
        Basket basketEntity = basketMapper.mapFrom(basketDTO);
        Basket updatedBasket = basketService.save(basketEntity);
        return new ResponseEntity<>(basketMapper.mapTo(updatedBasket), HttpStatus.OK);
    }

    @DeleteMapping(path = "/baskets/{id}")
    public ResponseEntity deletBasket(@PathVariable("id") Long id) {
        basketService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
