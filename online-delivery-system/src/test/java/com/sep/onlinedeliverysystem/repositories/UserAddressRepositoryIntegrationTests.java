package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import com.sep.onlinedeliverysystem.domain.entities.User;
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
public class UserAddressRepositoryIntegrationTests {

    private UserRepository userTest;
    private AddressRepository userAddressTest;

    @Autowired
    public UserAddressRepositoryIntegrationTests(AddressRepository test, UserRepository userTest){
        this.userAddressTest = test;
        this.userTest = userTest;
    }

    @Test
    public void testSingleAddressCreationAndFind(){
        User userEntity = TestUtil.userBuild1();
        userTest.save(userEntity);
        UserAddress userAddress = TestUtil.userAddressBuild1(userEntity);
        userAddressTest.save(userAddress);
        Optional<UserAddress> result = userAddressTest.findById(userAddress.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userAddress);
    }

    @Test
    public void testMultipleAddressCreationAndFind(){
        User userEntity = TestUtil.userBuild1();
        User userEntity2 = TestUtil.userBuild2();
        User userEntity3 = TestUtil.userBuild3();
        userTest.save(userEntity);
        userTest.save(userEntity2);
        userTest.save(userEntity3);
        UserAddress userAddress1 = TestUtil.userAddressBuild1(userEntity);
        UserAddress userAddress2 = TestUtil.userAddressBuild2(userEntity2);
        UserAddress userAddress3 = TestUtil.userAddressBuild3(userEntity3);
        userAddressTest.save(userAddress1);
        userAddressTest.save(userAddress2);
        userAddressTest.save(userAddress3);
        Iterable<UserAddress> result = userAddressTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(userAddress1, userAddress2, userAddress3);
    }

    @Test
    public void testAddressUpdate() {
        User userEntity = TestUtil.userBuild1();
        userTest.save(userEntity);
        UserAddress userAddress1 = TestUtil.userAddressBuild1(userEntity);
        userAddressTest.save(userAddress1);
        userAddress1.setPostCode("Y67 Z89");
        userAddressTest.save(userAddress1);
        Optional<UserAddress> result = userAddressTest.findById(userAddress1.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userAddress1);
    }

    @Test
    public void testAddressDelete() {
        User userEntity = TestUtil.userBuild1();
        userTest.save(userEntity);
        UserAddress userAddress1 = TestUtil.userAddressBuild1(userEntity);
        userAddressTest.save(userAddress1);
        userAddressTest.delete(userAddress1);
        Optional<UserAddress> result = userAddressTest.findById(userAddress1.getId());
        assertThat(result).isEmpty();
    }
}
