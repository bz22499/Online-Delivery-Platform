package com.sep.onlinedeliverysystem.dao;

import com.sep.onlinedeliverysystem.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void createUser(User user);
    Optional<User> findSingleId(long l);

    Optional<User> findSingleEmail(String email);

    List<User> find();

    void update(long id, User user);

    void delete(long l);
}
