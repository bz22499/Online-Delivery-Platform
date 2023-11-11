package com.sep.onlinedeliverysystem.dao;

import com.sep.onlinedeliverysystem.dao.impl.UserDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class UserDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDaoImpl underTest;

    @Test
    public void userCreationTest() {
        /*
        User user = User.builder()
            .id(1L)
            .first_name("Luke")
            .last_name("Trott")
            .email("john@trottmail.com")
            .password("password")
            .address_id(1L)
            .role(userRole.customer)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
         */
        underTest.createUser();
    }
}
