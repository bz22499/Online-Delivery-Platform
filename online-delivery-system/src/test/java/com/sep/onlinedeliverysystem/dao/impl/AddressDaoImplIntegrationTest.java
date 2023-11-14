package com.sep.onlinedeliverysystem.dao.impl;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.dao.UserDao;
import com.sep.onlinedeliverysystem.dao.implementation.AddressDaoImpl;
import com.sep.onlinedeliverysystem.model.Address;
import com.sep.onlinedeliverysystem.model.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressDaoImplIntegrationTest {

    private UserDao userDao;
    private AddressDaoImpl underTest;

    @Autowired
    public AddressDaoImplIntegrationTest(AddressDaoImpl underTest, UserDao userDao){
        this.underTest = underTest;
        this.userDao = userDao;
    }

    @Test
    public void testSingleAddressCreationAndFind(){
        User user = TestUtil.userBuild();
        userDao.createUser(user);
        Address address = TestUtil.addressBuild1();
        underTest.createAddress(address);
        Optional<Address> result = underTest.findSingle(address.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(address);
    }

    @Test
    public void testMultipleAddressCreationAndFind(){
        User user = TestUtil.userBuild();
        userDao.createUser(user);
        Address address1 = TestUtil.addressBuild1();
        Address address2 = TestUtil.addressBuild2();
        Address address3 = TestUtil.addressBuild3();
        address1.setUserId(user.getId());
        address2.setUserId(user.getId());
        address3.setUserId(user.getId());
        underTest.createAddress(address1);
        underTest.createAddress(address2);
        underTest.createAddress(address3);
        List<Address> result = underTest.find();
        assertThat(result)
                .hasSize(3)
                .containsExactly(address1, address2, address3);
    }
}
