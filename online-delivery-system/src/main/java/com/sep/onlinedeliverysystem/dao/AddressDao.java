package com.sep.onlinedeliverysystem.dao;

import com.sep.onlinedeliverysystem.model.Address;
import com.sep.onlinedeliverysystem.model.User;

import java.util.List;
import java.util.Optional;

public interface AddressDao {
    void createAddress(Address address);

    Optional<Address> findSingle(long l);
    List<Address> find();
}
