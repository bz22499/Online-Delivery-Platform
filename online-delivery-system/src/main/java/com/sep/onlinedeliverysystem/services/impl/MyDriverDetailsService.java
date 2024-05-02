package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Driver;
import com.sep.onlinedeliverysystem.domain.entities.DriverDetailsEntity;
import com.sep.onlinedeliverysystem.repositories.DriverRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("driverDetailsService")
public class MyDriverDetailsService implements UserDetailsService {

    private final DriverRepository driverRepository;

    public MyDriverDetailsService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Driver> driverOptional = driverRepository.findById(email);
        Driver driver = driverOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (driver == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        DriverDetailsEntity driverDetails = driver.toDriverDetails();

        return org.springframework.security.core.userdetails.User
                .withUsername(driver.getEmail())
                .password(driver.getPassword())
                .authorities("DRIVER")
                .accountExpired(!driverDetails.isAccountNonExpired())
                .accountLocked(!driverDetails.isAccountNonLocked())
                .credentialsExpired(!driverDetails.isCredentialsNonExpired())
                .disabled(!driverDetails.isEnabled())
                .build();
    }
}

