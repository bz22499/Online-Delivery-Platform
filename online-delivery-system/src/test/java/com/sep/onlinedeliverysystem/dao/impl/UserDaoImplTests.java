package com.sep.onlinedeliverysystem.dao.impl;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.dao.implementation.UserDaoImpl;
import com.sep.onlinedeliverysystem.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;




@ExtendWith(MockitoExtension.class)
public class UserDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDaoImpl underTest;

    @Test
    public void userCreationTest() {
        User user = TestUtil.userBuild();
        underTest.createUser(user);
        verify(jdbcTemplate).update(
                eq("INSERT INTO users (id, first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)"),
                eq(1L),
                eq("Luke"),
                eq("Trott"),
                eq("luke@trottmail.com"),
                eq("password"),
                eq("customer")
        );
    }

    @Test
    public void findSingleTest(){
        underTest.findSingle(1L);
        verify(jdbcTemplate).query(
                eq("SELECT id, first_name, last_name, email, password, role FROM users WHERE id = ? LIMIT 1"),
                ArgumentMatchers.<UserDaoImpl.UserRowMapper>any(),
                eq(1L)
        );
    }
}
