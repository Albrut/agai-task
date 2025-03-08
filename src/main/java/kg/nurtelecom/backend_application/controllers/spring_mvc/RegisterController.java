package kg.nurtelecom.backend_application.controllers.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.SaveUserToBdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/registration")
public class RegisterController {
    private final SaveUserToBdService saveUserToBdService;

    public RegisterController(SaveUserToBdService saveUserToBdService) {
        this.saveUserToBdService = saveUserToBdService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userSaveRequest", new UserSaveRequestForm());
        return "registration_and_login";
    }

    @PostMapping
    public ResponseEntity<UserSaveRequestForm> registerUser(@Valid UserSaveRequestForm userSaveRequestForm) {
        saveUserToBdService.save(userSaveRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaveRequestForm);
    }
}
