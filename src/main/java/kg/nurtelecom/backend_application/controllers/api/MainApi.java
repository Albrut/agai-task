package kg.nurtelecom.backend_application.controllers.api;

import kg.nurtelecom.backend_application.jdbc.JdbcConnection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainApi {

    private final JdbcConnection jdbcConnection;

    public MainApi(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }


    @GetMapping
    public String index() {
        return "Hello World";
    }
}
