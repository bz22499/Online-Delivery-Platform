package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import com.sep.onlinedeliverysystem.repositories.AddressRepository;
import com.sep.onlinedeliverysystem.repositories.UserRepository;
import com.sep.onlinedeliverysystem.services.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    private AddressRepository addressRepository;
    private UserRepository userRepository;

    public UserAddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserAddress save(UserAddress userAddress) {
        return addressRepository.save(userAddress);
    }

    @Override
    public List<UserAddress> findAll() {
        return StreamSupport.stream(addressRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Optional<UserAddress> findOne(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return addressRepository.existsById(id);
    }

    @Override
    public UserAddress partialUpdate(Long id, UserAddress userAddress) {
        userAddress.setId(id);

        return addressRepository.findById(id).map(existingAddress ->{
            Optional.ofNullable(userAddress.getCity()).ifPresent(existingAddress::setCity);
            Optional.ofNullable(userAddress.getCountry()).ifPresent(existingAddress::setCountry);
            Optional.ofNullable(userAddress.getPostCode()).ifPresent(existingAddress::setPostCode);
            Optional.ofNullable(userAddress.getStreet()).ifPresent(existingAddress::setStreet);
            return addressRepository.save(existingAddress);
        }).orElseThrow(() -> new RuntimeException("Address doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Optional<UserAddress> findByUserEmail(String email) {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            return addressRepository.findByUser(user.get());
        } else {
            return Optional.empty(); // Return empty optional if user with given email does not exist
        }
    }
}
