package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.Driver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface DriverService {
    Driver save(Driver driverEntity);

    List<Driver> findAll();

    Optional<Driver> findOne(String email);

    boolean Exists(String email);

    Driver partialUpdate(String email, Driver driverEntity);

    void delete(String email);

    boolean updateProfile(String email, String currentPassword, String newName, String newPassword);
}
