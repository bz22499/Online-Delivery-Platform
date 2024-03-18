package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.VendorAddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.VendorAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class VendorAddressController {

    private VendorAddressService vendorAddressService;

    private Mapper<VendorAddress, VendorAddressDTO> vendorAddressMapper;

    public VendorAddressController(VendorAddressService vendorAddressService, Mapper<VendorAddress, VendorAddressDTO> vendorAddressMapper){
        this.vendorAddressMapper = vendorAddressMapper;
        this.vendorAddressService = vendorAddressService;
    }

    //using DTOs to decouple service layer from persistence layer


    @PostMapping(path = "/vendorAddresses")
    public ResponseEntity<VendorAddressDTO> save(@RequestBody VendorAddressDTO vendorAddressDTO){ //Create functionality
        VendorAddress vendorAddress = vendorAddressMapper.mapFrom(vendorAddressDTO);
        VendorAddress savedVendorAddress = vendorAddressService.save(vendorAddress);
        return new ResponseEntity<>(vendorAddressMapper.mapTo(savedVendorAddress), HttpStatus.CREATED);
    }

    @GetMapping(path = "/vendorAddresses")
    public List<VendorAddressDTO> listVendorAddresses(){ //Read All functionality
        List<VendorAddress> addresses = vendorAddressService.findAll();
        return addresses.stream().map(vendorAddressMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/vendorAddresses/{id}") //Read One functionality
    public ResponseEntity<VendorAddressDTO> getAddress(@PathVariable("id") Long id){
        Optional<VendorAddress> foundAddress = vendorAddressService.findOne(id); //Use optional because either the address exists or it doesn't
        return foundAddress.map(addressEntity -> { //for if user exists
            VendorAddressDTO vendorAddressDTO = vendorAddressMapper.mapTo(addressEntity);
            return new ResponseEntity<>(vendorAddressDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if user doesn't exist
    }

    @PutMapping(path = "/vendorAddresses/{id}")
    public ResponseEntity<VendorAddressDTO> fullUpdateAddress(@PathVariable("id") Long id, @RequestBody VendorAddressDTO vendorAddressDTO){ //Full Update functionality
        if(!vendorAddressService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        vendorAddressDTO.setId(id);
        VendorAddress vendorAddress = vendorAddressMapper.mapFrom(vendorAddressDTO);
        VendorAddress savedVendorAddress = vendorAddressService.save(vendorAddress); //can reuse our create functionality to overwrite current address' info
        return new ResponseEntity<>(vendorAddressMapper.mapTo(savedVendorAddress), HttpStatus.OK);
    }

    @PatchMapping(path = "/vendorAddresses/{id}")
    public ResponseEntity<VendorAddressDTO> partialUpdate(@PathVariable("id") Long id, @RequestBody VendorAddressDTO vendorAddressDTO){ //Partial Update functionality
        if(!vendorAddressService.Exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VendorAddress vendorAddress = vendorAddressMapper.mapFrom(vendorAddressDTO);
        VendorAddress updatedAddress = vendorAddressService.partialUpdate(id, vendorAddress);
        return new ResponseEntity<>(vendorAddressMapper.mapTo(updatedAddress), HttpStatus.OK);
    }

    @DeleteMapping(path = "/vendorAddresses/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") Long id){
        vendorAddressService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/vendorAddresses/vendor/{email}")
    public ResponseEntity<VendorAddressDTO> getAddressByVendorEmail(@PathVariable String email) {
        Optional<VendorAddress> vendorAddressOptional = vendorAddressService.findByVendorEmail(email);
        if (vendorAddressOptional.isPresent()) {
            VendorAddressDTO vendorAddressDTO = vendorAddressMapper.mapTo(vendorAddressOptional.get());
            return new ResponseEntity<>(vendorAddressDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/vendorAddresses/vendor/{email}")
    public ResponseEntity<VendorAddressDTO> partialUpdateAddressByVendorEmail(@PathVariable String email, @RequestBody VendorAddressDTO address) {
        Optional<VendorAddress> vendorAddressOptional = vendorAddressService.findByVendorEmail(email);
        if (vendorAddressOptional.isPresent()) {
            VendorAddress vendorAddress = vendorAddressMapper.mapFrom(address);
            VendorAddress updatedAddress = vendorAddressService.partialUpdate(address.getId(), vendorAddress);
            return new ResponseEntity<>(vendorAddressMapper.mapTo(updatedAddress), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
