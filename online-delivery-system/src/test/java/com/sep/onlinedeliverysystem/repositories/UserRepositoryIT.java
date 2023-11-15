package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
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
public class UserRepositoryIT {

    private UserRepository test;

    @Autowired
    public UserRepositoryIT(UserRepository test){
        this.test = test;
    }

    @Test
    public void testSingleUserCreationAndFindId() {
        User user = TestUtil.userBuild1();
        test.save(user);
        Optional<User> result = test.findById(user.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
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
        User user1 = TestUtil.userBuild1();
        User user2 = TestUtil.userBuild2();
        User user3 = TestUtil.userBuild3();
        test.save(user1);
        test.save(user2);
        test.save(user3);
        Iterable<User> result = test.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(user1, user2, user3);
    }

    @Test
    public void testUserUpdate() {
        User user = TestUtil.userBuild1();
        test.save(user);
        user.setFirstName("Benedict");
        test.save(user);
        Optional<User> result = test.findById(user.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testUserDelete() {
        User user = TestUtil.userBuild1();
        test.save(user);
        test.delete(user);
        Optional<User> result = test.findById(user.getEmail());
        assertThat(result).isEmpty();
    }
}
