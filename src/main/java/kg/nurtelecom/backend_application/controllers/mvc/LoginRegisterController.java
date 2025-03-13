package kg.nurtelecom.backend_application.controllers.mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.payload.requests.UserSaveRequestForm;
import kg.nurtelecom.backend_application.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LoginRegisterController {

    private final AuthService authService;

    public LoginRegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userSaveRequest", new UserSaveRequestForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute UserSaveRequestForm userSaveRequestForm,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

       if (authService.userExist(userSaveRequestForm.getUsername())){
           return "redirect:/login";
       }
       if (authService.createUser(userSaveRequestForm.getUsername(), userSaveRequestForm.getPassword()) > 0){
           return "redirect:/login";
       }
        model.addAttribute("successMessage", "Registration Successful");
        return "login";
    }
}

