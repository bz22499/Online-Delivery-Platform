package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.entity.Address;
import com.sep.onlinedeliverysystem.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressRepositoryIT {

    private UserRepository user;
    private AddressRepository test;

    @Autowired
    public AddressRepositoryIT(AddressRepository test, UserRepository user){
        this.test = test;
        this.user = user;
    }

    @Test
    public void testSingleAddressCreationAndFind(){
        User user = TestUtil.userBuild1();
        Address address = TestUtil.addressBuild1(user);
        test.save(address);
        Optional<Address> result = test.findById(address.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(address);
    }

    @Test
    public void testMultipleAddressCreationAndFind(){
        User user = TestUtil.userBuild1();
        Address address1 = TestUtil.addressBuild1(user);
        Address address2 = TestUtil.addressBuild2(user);
        Address address3 = TestUtil.addressBuild3(user);
        test.save(address1);
        test.save(address2);
        test.save(address3);
        Iterable<Address> result = test.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(address1, address2, address3);
    }

    @Test
    public void testAddressUpdate() {
        User user = TestUtil.userBuild1();
        Address address1 = TestUtil.addressBuild1(user);
        test.save(address1);
        address1.setPostCode("Y67 Z89");
        test.save(address1);
        Optional<Address> result = test.findById(address1.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(address1);
    }

    @Test
    public void testAddressDelete() {
        User user = TestUtil.userBuild1();
        Address address1 = TestUtil.addressBuild1(user);
        test.save(address1);
        test.delete(address1);
        Optional<Address> result = test.findById(address1.getId());
        assertThat(result).isEmpty();
    }
}
