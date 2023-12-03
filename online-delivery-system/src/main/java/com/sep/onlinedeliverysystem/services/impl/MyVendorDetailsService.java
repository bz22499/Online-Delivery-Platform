package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.domain.entities.VendorDetailsEntity;
import com.sep.onlinedeliverysystem.repositories.VendorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("vendorDetailsService")
public class MyVendorDetailsService implements UserDetailsService {

    private final VendorRepository vendorRepository;

    public MyVendorDetailsService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Vendor> vendorOptional = vendorRepository.findById(email);
        Vendor vendor = vendorOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (vendor == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        VendorDetailsEntity vendorDetails = vendor.toVendorDetails();

        return org.springframework.security.core.userdetails.User
                .withUsername(vendor.getEmail())
                .password(vendor.getPassword())
                .authorities("VENDOR")
                .accountExpired(!vendorDetails.isAccountNonExpired())
                .accountLocked(!vendorDetails.isAccountNonLocked())
                .credentialsExpired(!vendorDetails.isCredentialsNonExpired())
                .disabled(!vendorDetails.isEnabled())
                .build();
    }
}

