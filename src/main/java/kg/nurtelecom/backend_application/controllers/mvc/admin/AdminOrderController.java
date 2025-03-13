package kg.nurtelecom.backend_application.controllers.mvc.admin;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.AdminOrderFacade;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final AdminOrderFacade adminOrderFacade;

    public AdminOrderController(AdminOrderFacade adminOrderFacade) {
        this.adminOrderFacade = adminOrderFacade;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        return adminOrderFacade.getAllOrders(model);
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable UUID id, Model model) {
        return adminOrderFacade.getOrderById(id, model);
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable UUID id) {
        return adminOrderFacade.deleteOrder(id);
    }

    @GetMapping("/search")
    public String searchOrders(@RequestParam UUID userId, Model model) {
        return adminOrderFacade.searchOrders(userId, model);
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable UUID id, Model model) {
        return adminOrderFacade.editOrderForm(id, model);
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(
            @PathVariable UUID id,
            @Valid @ModelAttribute("orderDetails") OrderDetailsRequest orderDetails) {

    return adminOrderFacade.updateOrder(id, orderDetails);
    }

    @GetMapping("/create")
    public String getCreateOrderPage(Model model) {
        return adminOrderFacade.getCreateOrderPage(model);
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute OrderItemsRequestsDTO orderItemsDTO) {
        return adminOrderFacade.createOrder(orderItemsDTO);
    }
}
