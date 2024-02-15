package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.repositories.MenuItemRepository;
import com.sep.onlinedeliverysystem.services.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }


    @Override
    public MenuItem save(MenuItem menuItemEntity) {
        return menuItemRepository.save(menuItemEntity);
    }

    @Override
    public List<MenuItem> findAll() {
       return StreamSupport.stream(menuItemRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public List<MenuItem> findByVendorEmail(String vendorId) {
        return StreamSupport.stream(menuItemRepository.findByVendorEmail(vendorId).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<MenuItem> findOne(Long id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public boolean Exists(Long id) {
        return menuItemRepository.existsById(id);
    }

    @Override
    public MenuItem partialUpdate(Long id, MenuItem menuItemEntity) {
        menuItemEntity.setId(id);

        return menuItemRepository.findById(id).map(existingMenuItem ->{
            Optional.ofNullable(menuItemEntity.getDescription()).ifPresent(existingMenuItem::setDescription);
            Optional.ofNullable(menuItemEntity.getPrice()).ifPresent(existingMenuItem::setPrice);
            Optional.ofNullable(menuItemEntity.getName()).ifPresent(existingMenuItem::setName);
            return menuItemRepository.save(existingMenuItem);
        }).orElseThrow(() -> new RuntimeException("Item doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }

    @Override
    public List<MenuItem> findByDeleteIsFalse() {
        return StreamSupport.stream(menuItemRepository.findByDeleteIsFalse().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findByDeleteIsTrue() {
        return StreamSupport.stream(menuItemRepository.findByDeleteIsTrue().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findByDeleteIsFalseAndVendorEmail(String vendorId) {
        return StreamSupport.stream(menuItemRepository.findByDeleteIsFalseAndVendorEmail(vendorId).spliterator(), false).collect(Collectors.toList());
    }
}
