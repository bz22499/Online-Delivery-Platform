package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import com.sep.onlinedeliverysystem.repositories.AddressRepository;
import com.sep.onlinedeliverysystem.services.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressEntity save(AddressEntity addressEntity) {
        return addressRepository.save(addressEntity);
    }

    @Override
    public List<AddressEntity> findAll() {
        return StreamSupport.stream(addressRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Optional<AddressEntity> findOne(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return addressRepository.existsById(id);
    }

    @Override
    public AddressEntity partialUpdate(Long id, AddressEntity addressEntity) {
        addressEntity.setId(id);

        return addressRepository.findById(id).map(existingAddress ->{
            Optional.ofNullable(addressEntity.getCity()).ifPresent(existingAddress::setCity);
            Optional.ofNullable(addressEntity.getCountry()).ifPresent(existingAddress::setCountry);
            Optional.ofNullable(addressEntity.getPostCode()).ifPresent(existingAddress::setPostCode);
            Optional.ofNullable(addressEntity.getStreet()).ifPresent(existingAddress::setStreet);
            return addressRepository.save(existingAddress);
        }).orElseThrow(() -> new RuntimeException("Address doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
