package kg.nurtelecom.backend_application.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

@Repository
public class CustomUserDetailsRepository implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT username, password, role FROM users WHERE username = ?";
        List<UserDetails> users = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> {
            String uname = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            return new User(uname, password, List.of(new SimpleGrantedAuthority(role)));
        });
        if(users.isEmpty()){
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return users.get(0);
    }
}
