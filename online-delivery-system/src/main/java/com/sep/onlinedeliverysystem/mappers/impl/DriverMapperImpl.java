package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.DriverDTO;
import com.sep.onlinedeliverysystem.domain.entities.Driver;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverMapperImpl implements Mapper<Driver, DriverDTO> {
    private ModelMapper modelMapper;

    public DriverMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DriverDTO mapTo(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }

    @Override
    public Driver mapFrom(DriverDTO driverDTO) {
        return modelMapper.map(driverDTO, Driver.class);
    }
}
