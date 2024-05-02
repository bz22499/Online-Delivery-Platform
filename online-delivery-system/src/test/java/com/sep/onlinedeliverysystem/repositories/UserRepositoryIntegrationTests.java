package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
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
public class UserRepositoryIntegrationTests {

    private UserRepository userTest;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository userTest){
        this.userTest = userTest;
    }

    @Test
    public void testSingleUserCreationAndFindId() {
        User userEntity = TestUtil.userBuild1();
        userTest.save(userEntity);
        Optional<User> result = userTest.findById(userEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void testMultipleUserCreationAndFind() {
        User userEntity1 = TestUtil.userBuild1();
        User userEntity2 = TestUtil.userBuild2();
        User userEntity3 = TestUtil.userBuild3();
        userTest.save(userEntity1);
        userTest.save(userEntity2);
        userTest.save(userEntity3);
        Iterable<User> result = userTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(userEntity1, userEntity2, userEntity3);
    }

    @Test
    public void testUserUpdate() {
        User userEntity = TestUtil.userBuild1();
        userTest.save(userEntity);
        userEntity.setFirstName("Benedict");
        userTest.save(userEntity);
        Optional<User> result = userTest.findById(userEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void testUserDelete() {
        User userEntity = TestUtil.userBuild1();
        userTest.save(userEntity);
        userTest.delete(userEntity);
        Optional<User> result = userTest.findById(userEntity.getEmail());
        assertThat(result).isEmpty();
    }
}
