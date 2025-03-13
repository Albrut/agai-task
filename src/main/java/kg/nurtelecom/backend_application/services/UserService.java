package kg.nurtelecom.backend_application.services;


import kg.nurtelecom.backend_application.payload.responses.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UUID findIdByUsername(String username);
    List<UserResponse> findAll();
    void deleteByUsername(String username);

}
