package kg.nurtelecom.backend_application.controllers.mvc.admin;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.AdminOrderFacade;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Validated
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final AdminOrderFacade adminOrderFacade;

    public AdminOrderController(AdminOrderFacade adminOrderFacade) {
        this.adminOrderFacade = adminOrderFacade;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        try {
            return adminOrderFacade.getAllOrders(model);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Error loading orders: " + exception.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable UUID id, Model model) {
        try {
            return adminOrderFacade.getOrderById(id, model);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Order not found: " + exception.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable UUID id, Model model) {
        try {
            adminOrderFacade.deleteOrder(id);
            return "redirect:/admin/orders";
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Deletion error: " + exception.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/search")
    public String searchOrders(@RequestParam UUID userId, Model model) {
        try {
            return adminOrderFacade.searchOrders(userId, model);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Search error: " + exception.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable UUID id, Model model) {
        try {
            return adminOrderFacade.editOrderForm(id, model);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Edit form error: " + exception.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(
            @PathVariable UUID id,
            @Valid @ModelAttribute("orderDetails") OrderDetailsRequest orderDetails,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", formatErrors(bindingResult));
            return "error-page";
        }

        try {
            return adminOrderFacade.updateOrder(id, orderDetails);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Update error: " + exception.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/create")
    public String getCreateOrderPage(Model model) {
        try {
            return adminOrderFacade.getCreateOrderPage(model);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Creation page error: " + exception.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/create")
    public String createOrder(
            @Valid @ModelAttribute OrderItemsRequestsDTO orderItemsDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", formatErrors(bindingResult));
            return "error-page";
        }

        try {
            return adminOrderFacade.createOrder(orderItemsDTO);
        } catch (Exception exception) {
            model.addAttribute("errorMessage", "Creation error: " + exception.getMessage());
            return "error-page";
        }
    }

    private String formatErrors(BindingResult bindingResult) {
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("Validation errors: ");

        for (ObjectError error : bindingResult.getAllErrors()) {
            errorBuilder.append(error.getDefaultMessage());
            errorBuilder.append("; ");
        }

        if (errorBuilder.length() > 0) {
            errorBuilder.setLength(errorBuilder.length() - 2);
        }

        return errorBuilder.toString();
    }
}