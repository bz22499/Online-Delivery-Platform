package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {}
