package com.sep.onlinedeliverysystem.dao.impl;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.dao.implementation.UserDaoImpl;
import com.sep.onlinedeliverysystem.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserDaoImplIntegrationTest {

    private UserDaoImpl underTest;

    @Autowired
    public UserDaoImplIntegrationTest(UserDaoImpl underTest){
        this.underTest = underTest;
    }

    @Test
    public void testUserCreation() {
        User user = TestUtil.userBuild();
        underTest.createUser(user);
        Optional<User> result = underTest.findSingle(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }
}
