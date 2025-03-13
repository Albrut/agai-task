package kg.nurtelecom.backend_application.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error-page")
public class ErrorPageController {
    @GetMapping
    public String errorPage() {
        return "error-page";
    }
}
