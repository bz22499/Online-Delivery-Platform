package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.DriverDTO;
import com.sep.onlinedeliverysystem.domain.dto.VendorDTO;
import com.sep.onlinedeliverysystem.domain.entities.Driver;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DriverController {

    private DriverService driverService;

    private Mapper<Driver, DriverDTO> driverMapper;

    public DriverController(DriverService driverService, Mapper<Driver, DriverDTO> driverMapper){
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    //using DTOs to decouple service layer from persistence layer!!!

    @PostMapping(path = "/drivers")
    public ResponseEntity<DriverDTO> save(@RequestBody DriverDTO driver){ //Create functionality
        Driver driverEntity = driverMapper.mapFrom(driver);
        Driver savedDriverEntity = driverService.save(driverEntity); //saves driver DTO as entity into our database
        return new ResponseEntity<>(driverMapper.mapTo(savedDriverEntity), HttpStatus.CREATED); //returns our saved entity as a DTO
    }

    @GetMapping(path = "/drivers")
    public List<DriverDTO> listDrivers(){ //Read All functionality
        List<Driver> drivers = driverService.findAll();
        return drivers.stream().map(driverMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/drivers/{email}") //Read One functionality
    public ResponseEntity<DriverDTO> getDriver(@PathVariable("email") String email){
        Optional<Driver> foundDriver = driverService.findOne(email); //Use optional because either the driver exists or it doesn't
        return foundDriver.map(driverEntity -> { //for if driver exists
            DriverDTO driverDTO = driverMapper.mapTo(driverEntity);
            return new ResponseEntity<>(driverDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if driver doesn't exist
    }

    @PutMapping(path = "/drivers/{email}")
    public ResponseEntity<DriverDTO> fullUpdateDriver(@PathVariable("email") String email, @RequestBody DriverDTO driverDTO){ //Full Update functionality
        if(!driverService.Exists(email)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        driverDTO.setEmail(email);
        Driver driverEntity = driverMapper.mapFrom(driverDTO);
        Driver savedDriverEntity = driverService.save(driverEntity); //can reuse our create functionality to overwrite current driver's info
        return new ResponseEntity<>(driverMapper.mapTo(savedDriverEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/drivers/{email}")
    public ResponseEntity<DriverDTO> partialUpdate(@PathVariable("email") String email, @RequestBody DriverDTO driverDTO){ //Partial Update functionality
        if(!driverService.Exists(email)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Driver driverEntity = driverMapper.mapFrom(driverDTO);
        Driver updatedDriver = driverService.partialUpdate(email, driverEntity);
        return new ResponseEntity<>(driverMapper.mapTo(updatedDriver), HttpStatus.OK);
    }

    @DeleteMapping(path = "/drivers/{email}")
    public ResponseEntity deleteDriver(@PathVariable("email") String email){
        driverService.delete(email);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/drivers/{email}/driverProfile")
    public ResponseEntity updateProfile(@PathVariable("email") String email, @RequestBody Map<String, String> requestBody) {
        String currentPassword = requestBody.get("currentPassword");
        String newName = requestBody.get("name");
        String newDescription = requestBody.get("description");
        String newPassword = requestBody.get("newPassword");

        if (!driverService.Exists(email)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Attempt to update the profile
        boolean updated = driverService.updateProfile(email, currentPassword, newName, newDescription, newPassword);

        if (updated) {
            // If profile updated successfully, return the updated user DTO
            Optional<Driver> updatedDriverOptional = driverService.findOne(email);
            if (updatedDriverOptional.isPresent()) {
                DriverDTO updatedDriverDTO = driverMapper.mapTo(updatedDriverOptional.get());
                return new ResponseEntity<>(updatedDriverDTO, HttpStatus.OK);
            }
        }

        // If the profile update failed (due to incorrect current password), return UNAUTHORIZED
        return new ResponseEntity<>("Current password is incorrect", HttpStatus.UNAUTHORIZED);
    }
}
