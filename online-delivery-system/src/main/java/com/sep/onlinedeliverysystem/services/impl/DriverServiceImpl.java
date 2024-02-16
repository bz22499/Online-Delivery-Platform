package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Driver;
import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.repositories.DriverRepository;
import com.sep.onlinedeliverysystem.services.DriverService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver save(Driver driverEntity) {
        return driverRepository.save(driverEntity);
    }

    @Override
    public List<Driver> findAll() {
       return StreamSupport.stream(driverRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<Driver> findOne(String email) {
        return driverRepository.findById(email);
    }

    @Override
    public boolean Exists(String email) {
        return driverRepository.existsById(email);
    }

    @Override
    public Driver partialUpdate(String email, Driver driverEntity) {
        driverEntity.setEmail(email);

        return driverRepository.findById(email).map(existingDriver ->{
            Optional.ofNullable(driverEntity.getRating()).ifPresent(existingDriver::setRating);
            Optional.ofNullable(driverEntity.getPassword()).ifPresent(existingDriver::setPassword);
            Optional.ofNullable(driverEntity.getName()).ifPresent(existingDriver::setName);
            return driverRepository.save(existingDriver);
        }).orElseThrow(() -> new RuntimeException("Driver doesn't exist"));
    }

    @Override
    public void delete(String email) {
        driverRepository.deleteById(email);
    }
}
