package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface VendorService {
    Vendor save(Vendor vendorEntity);

    List<Vendor> findAll();

    Page<Vendor> findAll(Pageable pageable);

    Optional<Vendor> findOne(String email);

    boolean Exists(String email);

    Vendor partialUpdate(String email, Vendor vendorEntity);

    void delete(String email);

    boolean updateProfile(String email, String currentPassword, String newFirstName, String newLastName, String newPassword);

}
