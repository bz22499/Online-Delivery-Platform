package com.sep.onlinedeliverysystem.dao.impl;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.dao.implementation.UserDaoImpl;
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
public class UserDaoImplIntegrationTest {

    private UserDaoImpl daoImplUnderTest;

    @Autowired
    public UserDaoImplIntegrationTest(UserDaoImpl daoImplUnderTest){
        this.daoImplUnderTest = daoImplUnderTest;
    }

    @Test
    public void testSingleUserCreationAndFindId() {
        User user = TestUtil.userBuild1();
        daoImplUnderTest.createUser(user);
        Optional<User> result = daoImplUnderTest.findSingleId(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testSingleUserCreationAndFindEmail() {
        User user = TestUtil.userBuild1();
        daoImplUnderTest.createUser(user);
        Optional<User> result = daoImplUnderTest.findSingleEmail(user.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testMultipleUserCreationAndFind() {
        User user1 = TestUtil.userBuild1();
        User user2 = TestUtil.userBuild2();
        User user3 = TestUtil.userBuild3();
        daoImplUnderTest.createUser(user1);
        daoImplUnderTest.createUser(user2);
        daoImplUnderTest.createUser(user3);
        List<User> result = daoImplUnderTest.find();
        assertThat(result)
                .hasSize(3)
                .containsExactly(user1, user2, user3);
    }

    @Test
    public void testUserUpdate() {
        User user = TestUtil.userBuild1();
        daoImplUnderTest.createUser(user);
        user.setFirst_name("Benedict");
        daoImplUnderTest.update(user.getId(), user);
        Optional<User> result = daoImplUnderTest.findSingleId(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testUserDelete() {
        User user = TestUtil.userBuild1();
        daoImplUnderTest.createUser(user);
        daoImplUnderTest.delete(user.getId());
        Optional<User> result = daoImplUnderTest.findSingleId(user.getId());
        assertThat(result).isEmpty();
    }
}
