package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.jdbc.JdbcConnection;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.UUID;

@Repository
public class UserSaverRepository implements UserService {
    private final JdbcConnection jdbcConnection;
    private static final String SQL_QUERY_FIND_BY_USERNAME_AND_GET_ID = "SELECT user_id FROM users WHERE username = ?";

    public UserSaverRepository(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    @Override
    public UUID findIdByUsername(String username) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_BY_USERNAME_AND_GET_ID);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getObject("user_id", UUID.class);
        }catch (SQLException sqlException){
            throw new RuntimeException("Cannot find user by username" + sqlException);
        }
    }
}
