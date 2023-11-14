package com.sep.onlinedeliverysystem.dao;

import com.sep.onlinedeliverysystem.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void createUser(User user);
    Optional<User> findSingle(long l);
    List<User> find();
}
