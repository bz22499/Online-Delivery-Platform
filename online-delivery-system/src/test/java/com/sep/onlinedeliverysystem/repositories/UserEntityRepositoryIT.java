package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
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
public class UserEntityRepositoryIT {

    private UserRepository test;

    @Autowired
    public UserEntityRepositoryIT(UserRepository test){
        this.test = test;
    }

    @Test
    public void testSingleUserCreationAndFindId() {
        UserEntity userEntity = TestUtil.userBuild1();
        test.save(userEntity);
        Optional<UserEntity> result = test.findById(userEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

//    @Test
//    public void testSingleUserCreationAndFindEmail() {
//        User user = TestUtil.userBuild1();
//        test.save(user);
//        Optional<User> result = test.findSingleEmail(user.getEmail());
//        assertThat(result).isPresent();
//        assertThat(result.get()).isEqualTo(user);
//    }

    @Test
    public void testMultipleUserCreationAndFind() {
        UserEntity userEntity1 = TestUtil.userBuild1();
        UserEntity userEntity2 = TestUtil.userBuild2();
        UserEntity userEntity3 = TestUtil.userBuild3();
        test.save(userEntity1);
        test.save(userEntity2);
        test.save(userEntity3);
        Iterable<UserEntity> result = test.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(userEntity1, userEntity2, userEntity3);
    }

    @Test
    public void testUserUpdate() {
        UserEntity userEntity = TestUtil.userBuild1();
        test.save(userEntity);
        userEntity.setFirstName("Benedict");
        test.save(userEntity);
        Optional<UserEntity> result = test.findById(userEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void testUserDelete() {
        UserEntity userEntity = TestUtil.userBuild1();
        test.save(userEntity);
        test.delete(userEntity);
        Optional<UserEntity> result = test.findById(userEntity.getEmail());
        assertThat(result).isEmpty();
    }
}
