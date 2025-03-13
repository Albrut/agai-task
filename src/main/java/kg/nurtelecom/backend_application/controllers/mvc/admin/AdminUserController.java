package kg.nurtelecom.backend_application.controllers.mvc.admin;

import kg.nurtelecom.backend_application.payload.responses.UserResponse;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/delete-user/{username}")
    public String deleteUserByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
        return "redirect:/admin/users";
    }
}
