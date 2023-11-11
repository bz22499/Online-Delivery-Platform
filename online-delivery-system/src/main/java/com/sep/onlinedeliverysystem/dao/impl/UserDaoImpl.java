package com.sep.onlinedeliverysystem.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements com.sep.onlinedeliverysystem.dao.UserDao{

    private JdbcTemplate jdbcTemplate;

    public UserDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createUser() {

    }
}
