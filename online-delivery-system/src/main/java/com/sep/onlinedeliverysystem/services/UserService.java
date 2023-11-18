package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserService {
    UserEntity save(UserEntity userEntity);

    List<UserEntity> findAll();

    Optional<UserEntity> findOne(String email);

    boolean Exists(String email);
}
