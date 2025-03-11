package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.jdbc.JdbcConnection;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class UserSaverRepository implements UserService {
    private final JdbcConnection jdbcConnection;
    private static final String SQL_QUERY_SAVE = "INSERT INTO users (username, password, email, updated_at, role) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_FIND_BY_ID = "SELECT * FROM users WHERE id = ?";

    public UserSaverRepository(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    @Override
    public void save(UserSaveRequestForm userSaveRequestForm) {

        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_SAVE);
            preparedStatement.setString(1, userSaveRequestForm.getUsername());
            preparedStatement.setString(2, userSaveRequestForm.getPassword());
            preparedStatement.setString(3, userSaveRequestForm.getEmail());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(userSaveRequestForm.getDateOfUpdate()));
            preparedStatement.setString(5, "ROLE_CLIENT");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        }catch (SQLException sqlException){
            throw new RuntimeException("Can't save user", sqlException);
        }

    }

    @Override
    public String findById(UUID id) {
       try {
           Connection connection = jdbcConnection.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_BY_ID);
           preparedStatement.setObject(1, id);
           ResultSet resultSet = preparedStatement.executeQuery();
           resultSet.next();
           return resultSet.getString("username");
       }catch (SQLException sqlException){
           throw new RuntimeException("Can't find user", sqlException);
       }
    }
}
