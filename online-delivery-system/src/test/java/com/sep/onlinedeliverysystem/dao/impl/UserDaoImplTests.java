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
    private UserDaoImpl daoImplUnderTest;

    @Test
    public void userCreationTest() {
        User user = TestUtil.userBuild1();
        daoImplUnderTest.createUser(user);
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
    public void findSingleIdTest(){
        daoImplUnderTest.findSingleId(1L);
        verify(jdbcTemplate).query(
                eq("SELECT id, first_name, last_name, email, password, role FROM users WHERE id = ? LIMIT 1"),
                ArgumentMatchers.<UserDaoImpl.UserRowMapper>any(),
                eq(1L)
        );
    }

    @Test public void findSingleEmailTest(){
        daoImplUnderTest.findSingleEmail("luke@trottmail.com");
        verify(jdbcTemplate).query(
                eq("SELECT id, first_name, last_name, email, password, role FROM users WHERE email = ? LIMIT 1"),
                ArgumentMatchers.<UserDaoImpl.UserRowMapper>any(),
                eq("luke@trottmail.com")
        );
    }

    @Test
    public void find(){
        daoImplUnderTest.find();
        verify(jdbcTemplate).query(
                eq("SELECT id, first_name, last_name, email, password, role FROM users"),
                ArgumentMatchers.<UserDaoImpl.UserRowMapper>any()
        );
    }

    @Test
    public void updateTest(){
        User user = TestUtil.userBuild1();
        daoImplUnderTest.update(4L, user);
        verify(jdbcTemplate).update(
                "UPDATE users SET id = ?, first_name = ?, last_name = ?, email = ?, password = ?, role = ? WHERE id = ?",
                 1l,
                "Luke",
                "Trott",
                "luke@trottmail.com",
                "password",
                "customer",
                4L
        );
    }

    @Test
    public void deleteTest(){
        daoImplUnderTest.delete(1L);
        verify(jdbcTemplate).update(
                "DELETE FROM users where id = ?",
                1L
        );
    }
}
