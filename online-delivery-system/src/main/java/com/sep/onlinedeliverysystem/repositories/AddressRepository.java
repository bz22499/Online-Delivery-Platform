package com.sep.onlinedeliverysystem.repositories;


import com.sep.onlinedeliverysystem.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {}
