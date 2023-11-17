package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.AddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

    private AddressService addressService;

    private Mapper<AddressEntity, AddressDTO> addressMapper;

    public AddressController(AddressService addressService, Mapper<AddressEntity, AddressDTO> addressMapper){
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @PostMapping(path = "/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO address){ //using DTOs to decouple service layer from persistence layer
        AddressEntity addressEntity = addressMapper.mapFrom(address);
        AddressEntity savedAddressEntity = addressService.createAddress(addressEntity);
        return new ResponseEntity<>(addressMapper.mapTo(savedAddressEntity), HttpStatus.CREATED);
    }

}
