package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public interface AddressService {
    AddressEntity createAddress(AddressEntity addressEntity);
}
