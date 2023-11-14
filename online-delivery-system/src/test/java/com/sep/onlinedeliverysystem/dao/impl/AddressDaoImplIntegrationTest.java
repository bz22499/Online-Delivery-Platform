package com.sep.onlinedeliverysystem.dao.impl;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.dao.UserDao;
import com.sep.onlinedeliverysystem.dao.implementation.AddressDaoImpl;
import com.sep.onlinedeliverysystem.model.Address;
import com.sep.onlinedeliverysystem.model.User;
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
    private AddressDaoImpl daoImplUnderTest;

    @Autowired
    public AddressDaoImplIntegrationTest(AddressDaoImpl daoImplUnderTest, UserDao userDao){
        this.daoImplUnderTest = daoImplUnderTest;
        this.userDao = userDao;
    }

    @Test
    public void testSingleAddressCreationAndFind(){
        User user = TestUtil.userBuild1();
        userDao.createUser(user);
        Address address = TestUtil.addressBuild1();
        address.setUserId(user.getId());
        daoImplUnderTest.createAddress(address);
        Optional<Address> result = daoImplUnderTest.findSingle(address.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(address);
    }

    @Test
    public void testMultipleAddressCreationAndFind(){
        User user = TestUtil.userBuild1();
        userDao.createUser(user);
        Address address1 = TestUtil.addressBuild1();
        Address address2 = TestUtil.addressBuild2();
        Address address3 = TestUtil.addressBuild3();
        address1.setUserId(user.getId());
        address2.setUserId(user.getId());
        address3.setUserId(user.getId());
        daoImplUnderTest.createAddress(address1);
        daoImplUnderTest.createAddress(address2);
        daoImplUnderTest.createAddress(address3);
        List<Address> result = daoImplUnderTest.find();
        assertThat(result)
                .hasSize(3)
                .containsExactly(address1, address2, address3);
    }

    @Test
    public void testAddressUpdate() {
        User user = TestUtil.userBuild1();
        userDao.createUser(user);
        Address address1 = TestUtil.addressBuild1();
        address1.setUserId(user.getId());
        daoImplUnderTest.createAddress(address1);
        address1.setPostCode("Y67 Z89");
        daoImplUnderTest.update(address1.getId(), address1);
        Optional<Address> result = daoImplUnderTest.findSingle(address1.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(address1);
    }

    @Test
    public void testAddressDelete() {
        User user = TestUtil.userBuild1();
        userDao.createUser(user);
        Address address1 = TestUtil.addressBuild1();
        address1.setUserId(user.getId());
        daoImplUnderTest.createAddress(address1);
        daoImplUnderTest.delete(address1.getId());
        Optional<Address> result = daoImplUnderTest.findSingle(address1.getId());
        assertThat(result).isEmpty();
    }
}
