package kg.nurtelecom.backend_application.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JdbcConnection {

    @Value("${spring.datasource.url}")
    private  String URL;

    @Value("${spring.datasource.username}")
    private  String USERNAME;

    @Value("${spring.datasource.password}")
    private  String PASSWORD;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (SQLException sqlException){
            throw new RuntimeException("Cannot connect to database", sqlException);
        }
    }
}
