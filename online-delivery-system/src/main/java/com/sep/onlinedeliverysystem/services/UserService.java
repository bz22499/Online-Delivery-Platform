package com.sep.onlinedeliverysystem.services;

import com.sep.onlinedeliverysystem.domain.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserService {
    User save(User userEntity);

    List<User> findAll();

    Optional<User> findOne(String email);

    boolean Exists(String email);

    User partialUpdate(String email, User userEntity);

    void delete(String email);
}
