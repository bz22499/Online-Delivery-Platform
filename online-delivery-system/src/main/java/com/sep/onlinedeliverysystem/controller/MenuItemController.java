package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.UserAddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.UserAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MenuItemController {

    private UserAddressService userAddressService;

    private Mapper<UserAddress, UserAddressDTO> addressMapper;

    public MenuItemController(UserAddressService userAddressService, Mapper<UserAddress, UserAddressDTO> addressMapper){
        this.userAddressService = userAddressService;
        this.addressMapper = addressMapper;
    }

    //using DTOs to decouple service layer from persistence layer


    @PostMapping(path = "/addresses")
    public ResponseEntity<UserAddressDTO> save(@RequestBody UserAddressDTO address){ //Create functionality
        UserAddress userAddress = addressMapper.mapFrom(address);
        UserAddress savedUserAddress = userAddressService.save(userAddress);
        return new ResponseEntity<>(addressMapper.mapTo(savedUserAddress), HttpStatus.CREATED);
    }

    @GetMapping(path = "/addresses")
    public List<UserAddressDTO> listAddresses(){ //Read All functionality
        List<UserAddress> addresses = userAddressService.findAll();
        return addresses.stream().map(addressMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/addresses/{id}") //Read One functionality
    public ResponseEntity<UserAddressDTO> getAddress(@PathVariable("id") Long id){
        Optional<UserAddress> foundAddress = userAddressService.findOne(id); //Use optional because either the address exists or it doesn't
        return foundAddress.map(addressEntity -> { //for if user exists
            UserAddressDTO userAddressDTO = addressMapper.mapTo(addressEntity);
            return new ResponseEntity<>(userAddressDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if user doesn't exist
    }

    @PutMapping(path = "/addresses/{id}")
    public ResponseEntity<UserAddressDTO> fullUpdateAddress(@PathVariable("id") Long id, @RequestBody UserAddressDTO userAddressDTO){ //Full Update functionality
        if(!userAddressService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userAddressDTO.setId(id);
        UserAddress userAddress = addressMapper.mapFrom(userAddressDTO);
        UserAddress savedUserAddress = userAddressService.save(userAddress); //can reuse our create functionality to overwrite current address' info
        return new ResponseEntity<>(addressMapper.mapTo(savedUserAddress), HttpStatus.OK);
    }

    @PatchMapping(path = "/addresses/{id}")
    public ResponseEntity<UserAddressDTO> partialUpdate(@PathVariable("id") Long id, @RequestBody UserAddressDTO userAddressDTO){ //Partial Update functionality
        if(!userAddressService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserAddress userAddress = addressMapper.mapFrom(userAddressDTO);
        UserAddress updatedAddress = userAddressService.partialUpdate(id, userAddress);
        return new ResponseEntity<>(addressMapper.mapTo(updatedAddress), HttpStatus.OK);
    }

    @DeleteMapping(path = "/addresses/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") Long id){
        userAddressService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
