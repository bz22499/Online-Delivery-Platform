package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.VendorDTO;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VendorMapperImpl implements Mapper<Vendor, VendorDTO> {
    private ModelMapper modelMapper;

    public VendorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public VendorDTO mapTo(Vendor vendor) {
        return modelMapper.map(vendor, VendorDTO.class);
    }

    @Override
    public Vendor mapFrom(VendorDTO vendorDTO) {
        return modelMapper.map(vendorDTO, Vendor.class);
    }
}
