package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserAddressService {
    UserAddress save(UserAddress userAddress);

    List<UserAddress> findAll();

    Optional<UserAddress> findOne(Long id);

    boolean Exists(Long id);

    UserAddress partialUpdate(Long id, UserAddress userAddressEntity);

    void delete(Long id);

    Optional<UserAddress> findByUserEmail(String email);

}
