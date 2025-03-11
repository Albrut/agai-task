package kg.nurtelecom.backend_application.services;

import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;

import java.util.UUID;

public interface UserService {
    void save(UserSaveRequestForm userSaveRequestForm);
    String findById(UUID id);
}
