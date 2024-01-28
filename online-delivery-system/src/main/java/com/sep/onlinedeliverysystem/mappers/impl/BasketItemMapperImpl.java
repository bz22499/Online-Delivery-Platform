package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.BasketItemDTO;
import com.sep.onlinedeliverysystem.domain.entities.BasketItem;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BasketItemMapperImpl implements Mapper<BasketItem, BasketItemDTO> {
    private ModelMapper modelMapper;

    public BasketItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BasketItemDTO mapTo(BasketItem basketItem) { return modelMapper.map(basketItem, BasketItemDTO.class);}

    public BasketItem mapFrom(BasketItemDTO basketItemDTO) { return modelMapper.map(basketItemDTO, BasketItem.class);}
}
