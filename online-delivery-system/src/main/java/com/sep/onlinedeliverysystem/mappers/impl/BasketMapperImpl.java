package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.BasketDTO;
import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BasketMapperImpl implements Mapper<Basket, BasketDTO> {
    private ModelMapper modelMapper;

    public BasketMapperImpl (ModelMapper modelMapper){ this.modelMapper = modelMapper; }

    @Override
    public BasketDTO mapTo(Basket basket) { return modelMapper.map(basket, BasketDTO.class); }

    @Override
    public Basket mapFrom(BasketDTO basketDTO) { return modelMapper.map(basketDTO, Basket.class); }
}
