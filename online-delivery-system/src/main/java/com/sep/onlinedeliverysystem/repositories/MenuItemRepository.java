package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
    List<MenuItem> findByVendorEmail(String vendorId);
}
