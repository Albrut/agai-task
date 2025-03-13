package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.mapper.UserDetailsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomUserDetailsRepository implements UserDetailsService {

    private final static String SQL_QUERY =
            "SELECT users.username, users.password, authorities.role " +
                    "FROM users " +
                    "JOIN authorities ON users.user_id = authorities.user_id " +
                    "WHERE users.username = ?";

    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = jdbcTemplate.query(
                SQL_QUERY,
                new Object[]{username},
                new UserDetailsRowMapper()
        );
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return users.get(0);
    }
}
