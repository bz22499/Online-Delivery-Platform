package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import com.sep.onlinedeliverysystem.repositories.UserRepository;
import com.sep.onlinedeliverysystem.services.UserService;
import org.hibernate.sql.model.jdbc.OptionalTableUpdateOperation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAll() {
       return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Optional<UserEntity> findOne(String email) {
        return userRepository.findById(email);
    }

    @Override
    public boolean Exists(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public UserEntity partialUpdate(String email, UserEntity userEntity) {
        userEntity.setEmail(email);

        return userRepository.findById(email).map(existingUser ->{
            Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
            Optional.ofNullable(userEntity.getFirstName()).ifPresent(existingUser::setFirstName);
            Optional.ofNullable(userEntity.getLastName()).ifPresent(existingUser::setLastName);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    @Override
    public void delete(String email) {
        userRepository.deleteById(email);
    }
}
