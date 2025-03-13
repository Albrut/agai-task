package kg.nurtelecom.backend_application.controllers.mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/registration")
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userSaveRequest", new UserSaveRequestForm());
        return "registration_and_login";
    }

    @PostMapping
    public ResponseEntity<UserSaveRequestForm> registerUser(@Valid @ModelAttribute UserSaveRequestForm userSaveRequestForm) {
        userService.save(userSaveRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaveRequestForm);
    }
}
