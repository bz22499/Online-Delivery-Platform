package com.sep.onlinedeliverysystem.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.repositories.VendorRepository;
import com.sep.onlinedeliverysystem.services.VendorService;

@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Vendor save(Vendor vendorEntity) {
        return vendorRepository.save(vendorEntity);
    }

    @Override
    public List<Vendor> findAll() {
       return StreamSupport.stream(vendorRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<Vendor> findAll(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    @Override
    public Optional<Vendor> findOne(String email) {
        return vendorRepository.findById(email);
    }

    @Override
    public boolean Exists(String email) {
        return vendorRepository.existsById(email);
    }

    @Override
    public Vendor partialUpdate(String email, Vendor vendorEntity) {
        vendorEntity.setEmail(email);

        return vendorRepository.findById(email).map(existingVendor ->{
            Optional.ofNullable(vendorEntity.getDescription()).ifPresent(existingVendor::setDescription);
            Optional.ofNullable(vendorEntity.getRating()).ifPresent(existingVendor::setRating);
            Optional.ofNullable(vendorEntity.getImageUrl()).ifPresent(existingVendor::setImageUrl);
            Optional.ofNullable(vendorEntity.getPassword()).ifPresent(existingVendor::setPassword);
            return vendorRepository.save(existingVendor);
        }).orElseThrow(() -> new RuntimeException("Vendor doesn't exist"));
    }

    @Override
    public void delete(String email) {
        vendorRepository.deleteById(email);
    }

    @Override
    public boolean updateProfile(String email, String currentPassword, String newName, String newDescription, String newPassword) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(email);
        if (vendorOptional.isPresent()) {
            Vendor vendor = vendorOptional.get();
            // Check if the current password matches
            if (currentPassword.equals(vendor.getPassword())) {
                // Update the first name and last name fields
                vendor.setName(newName);
                vendor.setDescription(newDescription);
                // Update the password if a new password is provided
                if (newPassword != null && !newPassword.isEmpty()) {
                    vendor.setPassword(newPassword);
                }
                vendorRepository.save(vendor);
                return true;
            }
        }
        return false;
    }
}
