package com.rodrigovsilva.demo.javarecords.merchant;

import com.rodrigovsilva.demo.javarecords.dto.Merchant;
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
public class MerchantDao {

    private JdbcTemplate jdbcTemplate;

    public MerchantDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // records work great with RowMapper
    private RowMapper<Website> websitesRowMapper = new RowMapper<>() {
        @Override
        public Website mapRow(ResultSet rs, int rowNum) throws SQLException {
            String url = rs.getString(2);
            String description = rs.getString(3);
            return new Website(url, description);
        }
    };

    private record MerchantDBRow(Long id, String username, String email) {
    }

    private RowMapper<MerchantDBRow> merchantRowMapper = new RowMapper<>() {
        @Override
        public MerchantDBRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong(1);
            String username = rs.getString(2);
            String email = rs.getString(3);
            return new MerchantDBRow(id, username, email);
        }
    };

    public Optional<Merchant> getMerchant(String username) {

        try {
            String merchantQuery = "SELECT * FROM merchants m WHERE m.username=?";
            MerchantDBRow merchant = jdbcTemplate.queryForObject(merchantQuery, merchantRowMapper, username);

            String websitesQuery = "SELECT * from merchant_websites web WHERE web.merchant_id=?";
            List<Website> websites = jdbcTemplate.query(websitesQuery, websitesRowMapper, merchant.id());

            return Optional.of(new Merchant(merchant.username(), merchant.email(), new HashSet<>(websites)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
