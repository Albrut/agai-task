package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.services.AuthService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class AuthRepository implements AuthService {
    private final JdbcClient jdbcClient;
    private final PasswordEncoder passwordEncoder;

    private static final String SQL_CHECK_USER = "SELECT COUNT(*) FROM users WHERE username = ?";

    private static final String SQL_CREATE_AUTHORITY =
            "INSERT INTO authorities (user_id, role) VALUES (?, ?)";

    private static final String SQL_CREATE_USER =
            "INSERT INTO users (username, password) VALUES ( ?, ?) RETURNING user_id";

    public AuthRepository(JdbcClient jdbcClient, PasswordEncoder passwordEncoder) {
        this.jdbcClient = jdbcClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean userExist(String username) {
        Integer count = jdbcClient.sql(SQL_CHECK_USER)
                .param(username)
                .query(Integer.class)
                .single();
        return count > 0;
    }
    @Transactional
    @Override
    public int createUser(String username, String password) {

        UUID userId = jdbcClient.sql(
                SQL_CREATE_USER).params(username, passwordEncoder.encode(password)).query(UUID.class).single();
        int authoritiesInserted = jdbcClient.sql(SQL_CREATE_AUTHORITY).params(userId, "ROLE_CLIENT").update();

        return authoritiesInserted;
    }

}