package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.OrderDTO;
import com.sep.onlinedeliverysystem.domain.entities.Order;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements Mapper<Order, OrderDTO> {
    private ModelMapper modelMapper;

    public OrderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDTO mapTo(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public Order mapFrom(OrderDTO orderDTO){
        return modelMapper.map(orderDTO, Order.class);
    }
}
