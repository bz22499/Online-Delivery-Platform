package com.sep.onlinedeliverysystem.dao.implementation;

import com.sep.onlinedeliverysystem.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoImpl implements com.sep.onlinedeliverysystem.dao.UserDao{

    private JdbcTemplate jdbcTemplate;

    public UserDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createUser(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (id, first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    @Override
    public Optional<User> findSingleId(long userId) {
        List<User> results = jdbcTemplate.query(
                "SELECT id, first_name, last_name, email, password, role FROM users WHERE id = ? LIMIT 1",
                new UserRowMapper(), userId);
        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findSingleEmail(String email){
        List<User> results = jdbcTemplate.query(
                "SELECT id, first_name, last_name, email, password, role FROM users WHERE email = ? LIMIT 1",
                new UserRowMapper(), email);
        return results.stream().findFirst();
    }

    @Override
    public List<User> find() {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name, email, password, role FROM users",
                new UserRowMapper());
    }

    @Override
    public void update(long id, User user) {
        jdbcTemplate.update(
                "UPDATE users SET id = ?, first_name = ?, last_name = ?, email = ?, password = ?, role = ? WHERE id = ?",
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                id
        );
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(
                "DELETE FROM users where id = ?",
                id
        );

    }

    public static class UserRowMapper implements RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .first_name(rs.getString("first_name"))
                    .last_name(rs.getString("last_name"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .role(rs.getString("role"))
                    .build();
        }
    }
}
