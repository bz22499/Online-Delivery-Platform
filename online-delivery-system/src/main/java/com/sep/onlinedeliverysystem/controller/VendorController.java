package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.dto.VendorDTO;
import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class VendorController {

    private VendorService vendorService;

    private Mapper<Vendor, VendorDTO> vendorMapper;

    public VendorController(VendorService vendorService, Mapper<Vendor, VendorDTO> vendorMapper){
        this.vendorService = vendorService;
        this.vendorMapper = vendorMapper;
    }

    //using DTOs to decouple service layer from persistence layer

    @PostMapping(path = "/vendors")
    public ResponseEntity<VendorDTO> save(@RequestBody VendorDTO vendorDTO){ //Create functionality
        Vendor vendorEntity = vendorMapper.mapFrom(vendorDTO);
        Vendor savedVendorEntity = vendorService.save(vendorEntity); //saves vendor DTO as entity into our database
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendorEntity), HttpStatus.CREATED); //returns our saved entity as a DTO
    }

    @GetMapping(path = "/vendors")
    public Page<VendorDTO> listVendors(Pageable pageable){ //Read All functionality
        Page<Vendor> vendors = vendorService.findAll(pageable);
        return vendors.map(vendorMapper::mapTo);
    }

    @GetMapping(path = "/vendors/{email}") //Read One functionality
    public ResponseEntity<VendorDTO> getVendor(@PathVariable("email") String email){
        Optional<Vendor> foundVendor = vendorService.findOne(email); //Use optional because either the vendor exists or it doesn't
        return foundVendor.map(vendorEntity -> { //for if vendor exists
            VendorDTO vendorDTO = vendorMapper.mapTo(vendorEntity);
            return new ResponseEntity<>(vendorDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if vendor doesn't exist
    }

    @PutMapping(path = "/vendors/{email}")
    public ResponseEntity<VendorDTO> fullUpdateVendor(@PathVariable("email") String email, @RequestBody VendorDTO vendorDTO){ //Full Update functionality
        if(!vendorService.Exists(email)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        vendorDTO.setEmail(email);
        Vendor vendorEntity = vendorMapper.mapFrom(vendorDTO);
        Vendor savedVendorEntity = vendorService.save(vendorEntity); //can reuse our create functionality to overwrite current vendor's info
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendorEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/vendors/{email}")
    public ResponseEntity<VendorDTO> partialUpdate(@PathVariable("email") String email, @RequestBody VendorDTO vendorDTO){ //Partial Update functionality
        if(!vendorService.Exists(email)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Vendor vendorEntity = vendorMapper.mapFrom(vendorDTO);
        Vendor updatedVendor = vendorService.partialUpdate(email, vendorEntity);
        return new ResponseEntity<>(vendorMapper.mapTo(updatedVendor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/vendors/{email}")
    public ResponseEntity deleteVendor(@PathVariable("email") String email){
        vendorService.delete(email);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/vendors/{email}/vendorProfile")
    public ResponseEntity updateProfile(@PathVariable("email") String email, @RequestBody Map<String, String> requestBody) {
        String currentPassword = requestBody.get("currentPassword");
        String newName = requestBody.get("name");
        String newDescription = requestBody.get("description");
        String newPassword = requestBody.get("newPassword");

        if (!vendorService.Exists(email)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Attempt to update the profile
        boolean updated = vendorService.updateProfile(email, currentPassword, newName, newDescription, newPassword);

        if (updated) {
            // If profile updated successfully, return the updated user DTO
            Optional<Vendor> updatedVendorOptional = vendorService.findOne(email);
            if (updatedVendorOptional.isPresent()) {
                VendorDTO updatedVendorDTO = vendorMapper.mapTo(updatedVendorOptional.get());
                return new ResponseEntity<>(updatedVendorDTO, HttpStatus.OK);
            }
        }

        // If the profile update failed (due to incorrect current password), return UNAUTHORIZED
        return new ResponseEntity<>("Current password is incorrect", HttpStatus.UNAUTHORIZED);
    }
}
