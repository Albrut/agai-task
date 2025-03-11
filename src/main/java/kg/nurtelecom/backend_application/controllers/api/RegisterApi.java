package kg.nurtelecom.backend_application.controllers.api;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/registration")
public class RegisterApi {

    private final UserService userService;

    public RegisterApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserSaveRequestForm> registerUser(@RequestBody @Valid UserSaveRequestForm userSaveRequestForm) {
        userService.save(userSaveRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaveRequestForm);
    }
}


