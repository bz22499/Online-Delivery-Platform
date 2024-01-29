package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public interface MenuItemService {
    MenuItem save(MenuItem menuItemEntity);

    List<MenuItem> findAll();

    List<MenuItem> findByVendorEmail(String vendorId);

    Optional<MenuItem> findOne(Long id);

    boolean Exists(Long id);

    MenuItem partialUpdate(Long id, MenuItem menuItemEntity);

    void delete(Long id);
}
