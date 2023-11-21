package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.AddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements Mapper<AddressEntity, AddressDTO> {

   private ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressDTO mapTo(AddressEntity addressEntity) {
        return modelMapper.map(addressEntity, AddressDTO.class);
    }

    @Override
    public AddressEntity mapFrom(AddressDTO addressDTO) {
        return modelMapper.map(addressDTO, AddressEntity.class);
    }
}
