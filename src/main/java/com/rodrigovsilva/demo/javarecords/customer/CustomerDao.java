package com.rodrigovsilva.demo.javarecords.customer;

import com.rodrigovsilva.demo.javarecords.dto.Customer;
import com.rodrigovsilva.demo.javarecords.dto.Website;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    public CustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // records work great with RowMapper
    private final RowMapper<Website> websitesRowMapper = (rs, rowNum) -> {
        String url = rs.getString(2);
        String description = rs.getString(3);
        return new Website(url, description);
    };

    private record CustomerDBRow(Long id, String username, String email) {
    }

    private RowMapper<CustomerDBRow> customerRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong(1);
        String username = rs.getString(2);
        String email = rs.getString(3);
        return new CustomerDBRow(id, username, email);
    };

    public Optional<Customer> getCustomer(String username) {

        try {
            String customerQuery = "SELECT * FROM customers m WHERE m.username=?";
            CustomerDBRow customer = jdbcTemplate.queryForObject(customerQuery, customerRowMapper, username);

            String websitesQuery = "SELECT * from customers_websites web WHERE web.customer_id=?";
            List<Website> websites = jdbcTemplate.query(websitesQuery, websitesRowMapper, customer.id());

            return Optional.of(new Customer(customer.username(), customer.email(), new HashSet<>(websites)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
