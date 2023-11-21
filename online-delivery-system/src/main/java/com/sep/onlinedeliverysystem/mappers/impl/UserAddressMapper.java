package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.UserAddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper implements Mapper<UserAddress, UserAddressDTO> {

   private ModelMapper modelMapper;

    public UserAddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserAddressDTO mapTo(UserAddress userAddress) {
        return modelMapper.map(userAddress, UserAddressDTO.class);
    }

    @Override
    public UserAddress mapFrom(UserAddressDTO userAddressDTO) {
        return modelMapper.map(userAddressDTO, UserAddress.class);
    }
}
