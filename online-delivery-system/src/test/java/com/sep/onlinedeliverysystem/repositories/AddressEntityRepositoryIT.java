package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
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
public class AddressEntityRepositoryIT {

    private UserRepository user;
    private AddressRepository test;

    @Autowired
    public AddressEntityRepositoryIT(AddressRepository test, UserRepository user){
        this.test = test;
        this.user = user;
    }

    @Test
    public void testSingleAddressCreationAndFind(){
        UserEntity userEntity = TestUtil.userBuild1();
        AddressEntity addressEntity = TestUtil.addressBuild1(userEntity);
        test.save(addressEntity);
        Optional<AddressEntity> result = test.findById(addressEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(addressEntity);
    }

    @Test
    public void testMultipleAddressCreationAndFind(){
        UserEntity userEntity = TestUtil.userBuild1();
        AddressEntity addressEntity1 = TestUtil.addressBuild1(userEntity);
        AddressEntity addressEntity2 = TestUtil.addressBuild2(userEntity);
        AddressEntity addressEntity3 = TestUtil.addressBuild3(userEntity);
        test.save(addressEntity1);
        test.save(addressEntity2);
        test.save(addressEntity3);
        Iterable<AddressEntity> result = test.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(addressEntity1, addressEntity2, addressEntity3);
    }

    @Test
    public void testAddressUpdate() {
        UserEntity userEntity = TestUtil.userBuild1();
        AddressEntity addressEntity1 = TestUtil.addressBuild1(userEntity);
        test.save(addressEntity1);
        addressEntity1.setPostCode("Y67 Z89");
        test.save(addressEntity1);
        Optional<AddressEntity> result = test.findById(addressEntity1.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(addressEntity1);
    }

    @Test
    public void testAddressDelete() {
        UserEntity userEntity = TestUtil.userBuild1();
        AddressEntity addressEntity1 = TestUtil.addressBuild1(userEntity);
        test.save(addressEntity1);
        test.delete(addressEntity1);
        Optional<AddressEntity> result = test.findById(addressEntity1.getId());
        assertThat(result).isEmpty();
    }
}
