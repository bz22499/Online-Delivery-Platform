package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import com.sep.onlinedeliverysystem.repositories.VendorAddressRepository;
import com.sep.onlinedeliverysystem.repositories.VendorRepository;
import com.sep.onlinedeliverysystem.services.VendorAddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VendorAddressServiceImpl implements VendorAddressService {

    private VendorAddressRepository vendorAddressRepository;
    private VendorRepository vendorRepository;

    public VendorAddressServiceImpl(VendorAddressRepository vendorAddressRepository, VendorRepository vendorRepository) {
        this.vendorAddressRepository = vendorAddressRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public VendorAddress save(VendorAddress vendorAddressEntity) {
        return vendorAddressRepository.save(vendorAddressEntity);
    }

    @Override
    public List<VendorAddress> findAll() {
       return StreamSupport.stream(vendorAddressRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Optional<VendorAddress> findOne(Long id) {
        return vendorAddressRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return vendorAddressRepository.existsById(id);
    }

    @Override
    public VendorAddress partialUpdate(Long id, VendorAddress vendorAddressEntity) {
        vendorAddressEntity.setId(id);

        return vendorAddressRepository.findById(id).map(existingVendorAddress ->{
            Optional.ofNullable(vendorAddressEntity.getCity()).ifPresent(existingVendorAddress::setCity);
            Optional.ofNullable(vendorAddressEntity.getStreet()).ifPresent(existingVendorAddress::setStreet);
            Optional.ofNullable(vendorAddressEntity.getCountry()).ifPresent(existingVendorAddress::setCountry);
            Optional.ofNullable(vendorAddressEntity.getPostCode()).ifPresent(existingVendorAddress::setPostCode);
            return vendorAddressRepository.save(existingVendorAddress);
        }).orElseThrow(() -> new RuntimeException("Vendor address doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        vendorAddressRepository.deleteById(id);
    }

    @Override
    public Optional<VendorAddress> findByVendorEmail(String email) {
        Optional<Vendor> vendor = vendorRepository.findById(email);
        if (vendor.isPresent()) {
            return vendorAddressRepository.findByVendor(vendor.get());
        } else {
            return Optional.empty(); // Return empty optional if user with given email does not exist
        }
    }
}
