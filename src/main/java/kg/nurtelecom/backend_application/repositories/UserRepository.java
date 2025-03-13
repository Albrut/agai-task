package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.jdbc.JdbcConnection;
import kg.nurtelecom.backend_application.payload.responses.UserResponse;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository implements UserService {
    private final JdbcConnection jdbcConnection;
    private static final String SQL_QUERY_FIND_BY_USERNAME_AND_GET_ID = "SELECT user_id FROM users WHERE username = ?";
    private static final String SQL_QUERY_DELETE_BY_USERNAME = "DELETE FROM users WHERE username = ?";

    private static final String SQL_QUERY_GET_ALL_USERS = "SELECT users.username, authorities.role " +
            "FROM users " +
            "JOIN authorities ON users.user_id = authorities.user_id";


    public UserRepository(JdbcConnection jdbcConnection) {
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

    @Override
    public List<UserResponse> findAll() {
        List<UserResponse> users = new ArrayList<>();

        try (Connection connection = jdbcConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_GET_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");

                UserResponse userResponse = new UserResponse(username, role);
                users.add(userResponse);
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException("Error fetching all users", sqlException);
        }

        return users;
    }

    @Override
    public void deleteByUsername(String username) {
        try (Connection connection = jdbcConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_BY_USERNAME)) {

            preparedStatement.setString(1, username);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("User not found for deletion: " + username);
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException("Error deleting user by username: " + username, sqlException);
        }
    }
}
