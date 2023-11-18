package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.AddressDTO;
import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AddressController {

    private AddressService addressService;

    private Mapper<AddressEntity, AddressDTO> addressMapper;

    public AddressController(AddressService addressService, Mapper<AddressEntity, AddressDTO> addressMapper){
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    //using DTOs to decouple service layer from persistence layer


    @PostMapping(path = "/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO address){ //Create functionality
        AddressEntity addressEntity = addressMapper.mapFrom(address);
        AddressEntity savedAddressEntity = addressService.createAddress(addressEntity);
        return new ResponseEntity<>(addressMapper.mapTo(savedAddressEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/addresses")
    public List<AddressDTO> listAddresses(){ //Read All functionality
        List<AddressEntity> addresses = addressService.findAll();
        return addresses.stream().map(addressMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/addresses/{id}") //Read One functionality
    public ResponseEntity<AddressDTO> getAddress(@PathVariable("id") long id){
        Optional<AddressEntity> foundAddress = addressService.findOne(id); //Use optional because either the user exists or it doesn't
        return foundAddress.map(addressEntity -> { //for if user exists
            AddressDTO addressDTO = addressMapper.mapTo(addressEntity);
            return new ResponseEntity<>(addressDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if user doesn't exist
    }

}
