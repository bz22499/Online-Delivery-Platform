package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface AddressService {
    AddressEntity save(AddressEntity addressEntity);

    List<AddressEntity> findAll();

    Optional<AddressEntity> findOne(Long id);

    boolean Exists(Long id);

    AddressEntity partialUpdate(Long id, AddressEntity addressEntity);

    void delete(Long id);
}
