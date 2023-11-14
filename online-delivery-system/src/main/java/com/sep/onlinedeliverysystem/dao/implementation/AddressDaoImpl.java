package com.sep.onlinedeliverysystem.dao.implementation;

import com.sep.onlinedeliverysystem.model.Address;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Component
public class AddressDaoImpl implements com.sep.onlinedeliverysystem.dao.AddressDao{
    private JdbcTemplate jdbcTemplate;

    public AddressDaoImpl(final JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public void createAddress(Address address) {
        jdbcTemplate.update(
                "INSERT INTO addresses (id, userId, street, city, postcode, country) VALUES (?, ?, ?, ?, ?, ?)",
                address.getId(),
                address.getUserId(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }

    @Override
    public Optional<Address> findSingle(long addressId) {
        List<Address> results = jdbcTemplate.query(
                "SELECT id, userId, street, city, postcode, country FROM addresses WHERE id = ? LIMIT 1",
                new AddressRowMapper(), addressId);
        return results.stream().findFirst();
    }

    @Override
    public List<Address> find() {
        return jdbcTemplate.query(
                "SELECT id, userId, street, city, postcode, country FROM addresses",
                new AddressRowMapper());
    }

    public static class AddressRowMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Address.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("userId"))
                    .street(rs.getString("street"))
                    .city(rs.getString("city"))
                    .postCode(rs.getString("postcode"))
                    .country(rs.getString("country"))
                    .build();
        }
    }
}
