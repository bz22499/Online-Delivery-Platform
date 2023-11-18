package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface AddressService {
    AddressEntity createAddress(AddressEntity addressEntity);

    List<AddressEntity> findAll();

    Optional<AddressEntity> findOne(long id);
}
