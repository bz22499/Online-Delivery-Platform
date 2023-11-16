package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    UserEntity createUser(UserEntity userEntity);
}
