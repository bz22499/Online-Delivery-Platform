package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import com.sep.onlinedeliverysystem.repositories.UserAddressRepository;
import com.sep.onlinedeliverysystem.repositories.UserRepository;
import com.sep.onlinedeliverysystem.services.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    
    private UserAddressRepository userAddressRepository;
    private UserRepository userRepository;

    public UserAddressServiceImpl(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public UserAddress save(UserAddress userAddress) {
        return userAddressRepository.save(userAddress);
    }

    @Override
    public List<UserAddress> findAll() {
        return StreamSupport.stream(userAddressRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Optional<UserAddress> findOne(Long id) {
        return userAddressRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return userAddressRepository.existsById(id);
    }

    @Override
    public UserAddress partialUpdate(Long id, UserAddress userAddress) {
        userAddress.setId(id);

        return userAddressRepository.findById(id).map(existingAddress ->{
            Optional.ofNullable(userAddress.getCity()).ifPresent(existingAddress::setCity);
            Optional.ofNullable(userAddress.getCountry()).ifPresent(existingAddress::setCountry);
            Optional.ofNullable(userAddress.getPostCode()).ifPresent(existingAddress::setPostCode);
            Optional.ofNullable(userAddress.getStreet()).ifPresent(existingAddress::setStreet);
            return userAddressRepository.save(existingAddress);
        }).orElseThrow(() -> new RuntimeException("Address doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        userAddressRepository.deleteById(id);
    }

    @Override
    public Optional<UserAddress> findByUserEmail(String email) {
        return userAddressRepository.findByUserEmail(email);
    }
}
