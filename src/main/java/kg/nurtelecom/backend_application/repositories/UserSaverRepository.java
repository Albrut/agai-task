package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.jdbc.JdbcConnection;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.SaveUserToBdService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;

@Repository
public class UserSaverRepository implements SaveUserToBdService {
    private final JdbcConnection jdbcConnection;
    private static final String SQL_QUERY = "INSERT INTO users (username, password, email, updated_at, role) VALUES (?, ?, ?, ?, ?)";

    public UserSaverRepository(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    @Override
    public void save(UserSaveRequestForm userSaveRequestForm) {

        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY);
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
}
