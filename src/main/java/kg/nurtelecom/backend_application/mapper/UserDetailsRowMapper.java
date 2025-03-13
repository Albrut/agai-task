package kg.nurtelecom.backend_application.mapper;

import kg.nurtelecom.backend_application.payload.requests.UserRequest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDetailsRowMapper implements RowMapper<UserDetails> {

    @Override
    public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String role = rs.getString("role");

        return new UserRequest(username, password, List.of(new SimpleGrantedAuthority(role)));
    }
}
