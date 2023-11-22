package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface VendorService {
    Vendor save(Vendor vendorEntity);

    List<Vendor> findAll();

    Optional<Vendor> findOne(String email);

    boolean Exists(String email);

    Vendor partialUpdate(String email, Vendor vendorEntity);

    void delete(String email);
}
