package com.sep.onlinedeliverysystem.repositories;


import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {}
