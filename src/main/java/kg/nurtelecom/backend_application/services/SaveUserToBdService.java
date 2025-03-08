package kg.nurtelecom.backend_application.services;

import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;

public interface SaveUserToBdService {
    void save(UserSaveRequestForm userSaveRequestForm);
}
