package com.sep.onlinedeliverysystem.controller;
import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import com.sep.onlinedeliverysystem.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private Mapper<UserEntity, UserDTO> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDTO> userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    //using DTOs to decouple service layer from persistence layer!!!

    @PostMapping(path = "/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){ //Create functionality
        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity savedUserEntity = userService.createUser(userEntity);
        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    public List<UserDTO> listUsers(){ //Read All functionality
        List<UserEntity> users = userService.findAll();
        return users.stream().map(userMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/users/{email}") //Read One functionality
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") String email){
        Optional<UserEntity> foundUser = userService.findOne(email); //Use optional because either the user exists or it doesn't
        return foundUser.map(userEntity -> { //for if user exists
            UserDTO userDTO = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //for if user doesn't exist
    }

}
