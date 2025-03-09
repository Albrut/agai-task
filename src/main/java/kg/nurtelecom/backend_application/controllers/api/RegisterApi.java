package kg.nurtelecom.backend_application.controllers.api;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.SaveUserToBdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/registration")
public class RegisterApi {

    private final SaveUserToBdService saveUserToBdService;

    public RegisterApi(SaveUserToBdService saveUserToBdService) {
        this.saveUserToBdService = saveUserToBdService;
    }

    @PostMapping
    public ResponseEntity<UserSaveRequestForm> registerUser(@RequestBody @Valid UserSaveRequestForm userSaveRequestForm) {
        saveUserToBdService.save(userSaveRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaveRequestForm);
    }
}


