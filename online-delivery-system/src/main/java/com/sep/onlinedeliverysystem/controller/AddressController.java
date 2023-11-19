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
    public ResponseEntity<AddressDTO> save(@RequestBody AddressDTO address){ //Create functionality
        AddressEntity addressEntity = addressMapper.mapFrom(address);
        AddressEntity savedAddressEntity = addressService.save(addressEntity);
        return new ResponseEntity<>(addressMapper.mapTo(savedAddressEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/addresses")
    public List<AddressDTO> listAddresses(){ //Read All functionality
        List<AddressEntity> addresses = addressService.findAll();
        return addresses.stream().map(addressMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/addresses/{id}") //Read One functionality
    public ResponseEntity<AddressDTO> getAddress(@PathVariable("id") Long id){
        Optional<AddressEntity> foundAddress = addressService.findOne(id); //Use optional because either the address exists or it doesn't
        return foundAddress.map(addressEntity -> { //for if user exists
            AddressDTO addressDTO = addressMapper.mapTo(addressEntity);
            return new ResponseEntity<>(addressDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if user doesn't exist
    }

    @PutMapping(path = "/addresses/{id}")
    public ResponseEntity<AddressDTO> fullUpdateAddress(@PathVariable("id") Long id, @RequestBody AddressDTO addressDTO){ //Full Update functionality
        if(!addressService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        addressDTO.setId(id);
        AddressEntity addressEntity = addressMapper.mapFrom(addressDTO);
        AddressEntity savedAddressEntity = addressService.save(addressEntity); //can reuse our create functionality to overwrite current address' info
        return new ResponseEntity<>(addressMapper.mapTo(savedAddressEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/addresses/{id}")
    public ResponseEntity<AddressDTO> partialUpdate(@PathVariable("id") Long id, @RequestBody AddressDTO addressDTO){ //Partial Update functionality
        if(!addressService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AddressEntity addressEntity = addressMapper.mapFrom(addressDTO);
        AddressEntity updatedAddress = addressService.partialUpdate(id, addressEntity);
        return new ResponseEntity<>(addressMapper.mapTo(updatedAddress), HttpStatus.OK);
    }

    @DeleteMapping(path = "/addresses/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") Long id){
        addressService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
