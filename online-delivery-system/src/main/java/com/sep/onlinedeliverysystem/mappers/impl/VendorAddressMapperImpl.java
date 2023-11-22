package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.VendorAddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VendorAddressMapperImpl implements Mapper<VendorAddress, VendorAddressDTO> {
    private ModelMapper modelMapper;

    public VendorAddressMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public VendorAddressDTO mapTo(VendorAddress vendorAddress) { return modelMapper.map(vendorAddress, VendorAddressDTO.class); }

    @Override
    public VendorAddress mapFrom(VendorAddressDTO vendorAddressDTO) { return modelMapper.map(vendorAddressDTO, VendorAddress.class); }
}
