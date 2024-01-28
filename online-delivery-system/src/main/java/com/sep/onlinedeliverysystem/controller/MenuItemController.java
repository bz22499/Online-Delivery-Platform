package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.MenuItemDTO;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MenuItemController {

    private MenuItemService menuItemService;

    private Mapper<MenuItem, MenuItemDTO> menuItemMapper;

    public MenuItemController(MenuItemService menuItemService, Mapper<MenuItem, MenuItemDTO> menuItemMapper){
        this.menuItemService = menuItemService;
        this.menuItemMapper = menuItemMapper;
    }

    //using DTOs to decouple service layer from persistence layer


    @PostMapping(path = "/menuItems")
    public ResponseEntity<MenuItemDTO> save(@RequestBody MenuItemDTO menuItemDTO){ //Create functionality
        MenuItem menuItem = menuItemMapper.mapFrom(menuItemDTO);
        MenuItem savedMenuItem = menuItemService.save(menuItem);
        return new ResponseEntity<>(menuItemMapper.mapTo(savedMenuItem), HttpStatus.CREATED);
    }

    @GetMapping(path = "/menuItems")
    public List<MenuItemDTO> listMenuItems(){ //Read All functionality
        List<MenuItem> menuItems = menuItemService.findAll();
        return menuItems.stream().map(menuItemMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/menuItems/{id}") //Read One functionality
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable("id") Long id){
        Optional<MenuItem> foundMenuItem = menuItemService.findOne(id); //Use optional because either the item exists or it doesn't
        return foundMenuItem.map(menuItemEntity -> { //for if user exists
            MenuItemDTO menuItemDTO = menuItemMapper.mapTo(menuItemEntity);
            return new ResponseEntity<>(menuItemDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if item doesn't exist
    }

    @GetMapping(path = "/menuItems/vendor/{vendorId}") //Read All from Vendor functionality
    public List<MenuItemDTO> listMenuItemsByVendor(@PathVariable("vendorId") String vendorId){
        List<MenuItem> menuItems = menuItemService.findByVendorEmail(vendorId);
        return menuItems.stream().map(menuItemMapper::mapTo).collect(Collectors.toList());
    }

    @PutMapping(path = "/menuItems/{id}")
    public ResponseEntity<MenuItemDTO> fullUpdateMenuItem(@PathVariable("id") Long id, @RequestBody MenuItemDTO menuItemDTO){ //Full Update functionality
        if(!menuItemService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        menuItemDTO.setId(id);
        MenuItem menuItemEntity = menuItemMapper.mapFrom(menuItemDTO);
        MenuItem savedMenuItem = menuItemService.save(menuItemEntity); //can reuse our create functionality to overwrite current item's info
        return new ResponseEntity<>(menuItemMapper.mapTo(savedMenuItem), HttpStatus.OK);
    }

    @PatchMapping(path = "/menuItems/{id}")
    public ResponseEntity<MenuItemDTO> partialUpdate(@PathVariable("id") Long id, @RequestBody MenuItemDTO menuItemDTO){ //Partial Update functionality
        if(!menuItemService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MenuItem menuItemEntity = menuItemMapper.mapFrom(menuItemDTO);
        MenuItem updatedMenuItem = menuItemService.partialUpdate(id, menuItemEntity);
        return new ResponseEntity<>(menuItemMapper.mapTo(updatedMenuItem), HttpStatus.OK);
    }

    @DeleteMapping(path = "/menuItems/{id}")
    public ResponseEntity deleteMenuItem(@PathVariable("id") Long id){
        menuItemService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
