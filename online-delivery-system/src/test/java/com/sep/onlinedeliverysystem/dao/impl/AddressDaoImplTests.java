package com.sep.onlinedeliverysystem.dao.impl;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.dao.implementation.AddressDaoImpl;
import com.sep.onlinedeliverysystem.model.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AddressDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AddressDaoImpl underTest;

    @Test
    public void addressCreationTest(){
        Address address = TestUtil.addressBuild1();
        underTest.createAddress(address);
        verify(jdbcTemplate).update(
                eq("INSERT INTO addresses (id, userId, street, city, postcode, country) VALUES (?, ?, ?, ?, ?, ?)"),
                eq(2L),
                eq(1L),
                eq("123 Kiggell Road"),
                eq("Bristol"),
                eq("A12 B34"),
                eq("Wales")
        );
    }

    @Test
    public void findSingleTest(){
        underTest.findSingle(2L);
        verify(jdbcTemplate).query(
                eq("SELECT id, userId, street, city, postcode, country FROM addresses WHERE id = ? LIMIT 1"),
                ArgumentMatchers.<AddressDaoImpl.AddressRowMapper>any(),
                eq(2L)
        );
    }

    @Test
    public void find(){
        underTest.find();
        verify(jdbcTemplate).query(
                eq("SELECT id, userId, street, city, postcode, country FROM addresses"),
                ArgumentMatchers.<AddressDaoImpl.AddressRowMapper>any()
        );
    }
}
