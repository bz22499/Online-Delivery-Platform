package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorAddressRepository extends CrudRepository<VendorAddress, Long> {
    Optional<VendorAddress> findByVendor(Vendor vendor);
}
