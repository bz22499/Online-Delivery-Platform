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
    private AddressDaoImpl daoImplUnderTest;

    @Test
    public void addressCreationTest(){
        Address address = TestUtil.addressBuild1();
        daoImplUnderTest.createAddress(address);
        verify(jdbcTemplate).update(
                eq("INSERT INTO addresses (id, userId, street, city, postcode, country) VALUES (?, ?, ?, ?, ?, ?)"),
                eq(100L),
                eq(0L),
                eq("123 Kiggell Road"),
                eq("Bristol"),
                eq("A12 B34"),
                eq("Wales")
        );
    }

    @Test
    public void findSingleTest(){
        daoImplUnderTest.findSingle(100L);
        verify(jdbcTemplate).query(
                eq("SELECT id, userId, street, city, postcode, country FROM addresses WHERE id = ? LIMIT 1"),
                ArgumentMatchers.<AddressDaoImpl.AddressRowMapper>any(),
                eq(100L)
        );
    }

    @Test
    public void findTest(){
        daoImplUnderTest.find();
        verify(jdbcTemplate).query(
                eq("SELECT id, userId, street, city, postcode, country FROM addresses"),
                ArgumentMatchers.<AddressDaoImpl.AddressRowMapper>any()
        );
    }

    @Test
    public void updateTest(){
        Address address = TestUtil.addressBuild1();
        daoImplUnderTest.update(400L, address);
        verify(jdbcTemplate).update(
                "UPDATE addresses SET id = ?, userId = ?, street = ?, city = ?, postcode = ?, country = ? WHERE id = ?",
                100L,
                0L,
                "123 Kiggell Road",
                "Bristol",
                "A12 B34",
                "Wales",
                400L
        );
    }

    @Test
    public void deleteTest(){
        daoImplUnderTest.delete(100L);
        verify(jdbcTemplate).update(
                "DELETE FROM addresses where id = ?",
                100L
        );
    }
}
