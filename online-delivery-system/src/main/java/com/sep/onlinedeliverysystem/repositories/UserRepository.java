package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {}
