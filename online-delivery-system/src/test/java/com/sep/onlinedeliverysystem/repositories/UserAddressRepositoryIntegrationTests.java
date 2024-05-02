package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
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

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressTest;

    @Autowired
    public UserAddressRepositoryIntegrationTests(UserAddressRepository userAddressTest, UserRepository userRepository){
        this.userAddressTest = userAddressTest;
        this.userRepository = userRepository;
    }

    @Test
    public void testSingleAddressCreationAndFind(){
        User user = TestUtil.userBuild1();
        User savedUser = userRepository.save(user);
        UserAddress userAddress = TestUtil.userAddressBuild1(savedUser);
        userAddressTest.save(userAddress);
        Optional<UserAddress> result = userAddressTest.findById(userAddress.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userAddress);
    }

    @Test
    public void testMultipleAddressCreationAndFind(){
        User user = TestUtil.userBuild1();
        User savedUser = userRepository.save(user);
        UserAddress userAddress1 = TestUtil.userAddressBuild1(savedUser);
        UserAddress userAddress2 = TestUtil.userAddressBuild2(savedUser);
        UserAddress userAddress3 = TestUtil.userAddressBuild3(savedUser);
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
        User user = TestUtil.userBuild1();
        User savedUser = userRepository.save(user);
        UserAddress userAddress1 = TestUtil.userAddressBuild1(savedUser);
        userAddressTest.save(userAddress1);
        userAddress1.setPostCode("Y67 Z89");
        userAddressTest.save(userAddress1);
        Optional<UserAddress> result = userAddressTest.findById(userAddress1.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userAddress1);
    }

    @Test
    public void testAddressDelete() {
        User user = TestUtil.userBuild1();
        User savedUser = userRepository.save(user);
        UserAddress userAddress1 = TestUtil.userAddressBuild1(savedUser);
        userAddressTest.save(userAddress1);
        userAddressTest.delete(userAddress1);
        Optional<UserAddress> result = userAddressTest.findById(userAddress1.getId());
        assertThat(result).isEmpty();
    }
}
