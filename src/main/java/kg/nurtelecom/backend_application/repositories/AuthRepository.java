package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.services.AuthService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AuthRepository implements AuthService {
    private final JdbcClient jdbcClient;
    private final PasswordEncoder passwordEncoder;

    private static final String SQL_CHECK_USER = "SELECT COUNT(*) FROM users WHERE username = ?";
    private static final String SQL_CREATE_USER =
            "INSERT INTO users (user_id, username, password, role) VALUES (?, ?, ?, ?)";

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

    @Override
    public int createUser(String username, String password) {
       return jdbcClient.sql(SQL_CREATE_USER)
                .params(
                        UUID.randomUUID(),
                        username,
                        passwordEncoder.encode(password),
                        "ROLE_CLIENT"
                )
                .update();
    }
}