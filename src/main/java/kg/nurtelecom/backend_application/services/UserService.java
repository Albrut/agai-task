package kg.nurtelecom.backend_application.services;


import java.util.UUID;

public interface UserService {
    UUID findIdByUsername(String username);
}
