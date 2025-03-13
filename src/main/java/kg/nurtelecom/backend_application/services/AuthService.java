package kg.nurtelecom.backend_application.services;

public interface AuthService {
    Boolean userExist(String username);
    int createUser(String username, String password);
}
