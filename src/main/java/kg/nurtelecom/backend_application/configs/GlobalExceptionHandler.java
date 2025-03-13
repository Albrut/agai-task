package kg.nurtelecom.backend_application.configs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException ex, RedirectAttributes redirectAttributes) {
        StringBuilder errors = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.append(violation.getMessage()).append("; ");
        }
        redirectAttributes.addFlashAttribute("errorMessage", errors.toString());
        return "redirect:/error-page";
    }
}

