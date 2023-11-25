package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.repositories.VendorRepository;
import com.sep.onlinedeliverysystem.services.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
       return StreamSupport.stream(vendorRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Page<Vendor> findAll(Pageable pageable) {return vendorRepository.findAll(pageable); }

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
            return vendorRepository.save(existingVendor);
        }).orElseThrow(() -> new RuntimeException("Vendor doesn't exist"));
    }

    @Override
    public void delete(String email) {
        vendorRepository.deleteById(email);
    }
}
