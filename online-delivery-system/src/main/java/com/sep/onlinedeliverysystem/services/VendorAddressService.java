package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface VendorAddressService {
    VendorAddress save(VendorAddress vendorAddressEntity);

    List<VendorAddress> findAll();

    Optional<VendorAddress> findOne(Long id);

    boolean Exists(Long id);

    VendorAddress partialUpdate(Long id, VendorAddress vendorAddressEntity);

    void delete(Long id);

    Optional<VendorAddress> findByVendorEmail(String email);

}
