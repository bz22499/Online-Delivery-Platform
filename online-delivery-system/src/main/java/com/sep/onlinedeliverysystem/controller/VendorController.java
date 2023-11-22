package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.domain.dto.VendorDTO;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    //using DTOs to decouple service layer from persistence layer!!!

    @PostMapping(path = "/vendors")
    public ResponseEntity<VendorDTO> save(@RequestBody VendorDTO vendorDTO){ //Create functionality
        Vendor vendorEntity = vendorMapper.mapFrom(vendorDTO);
        Vendor savedVendorEntity = vendorService.save(vendorEntity); //saves vendor DTO as entity into our database
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendorEntity), HttpStatus.CREATED); //returns our saved entity as a DTO
    }

    @GetMapping(path = "/vendors")
    public List<VendorDTO> listVendors(){ //Read All functionality
        List<Vendor> vendors = vendorService.findAll();
        return vendors.stream().map(vendorMapper::mapTo).collect(Collectors.toList());
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
}
