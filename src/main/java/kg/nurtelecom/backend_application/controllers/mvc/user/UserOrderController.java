package kg.nurtelecom.backend_application.controllers.mvc.user;

import kg.nurtelecom.backend_application.facades.UserOrderFacade;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import kg.nurtelecom.backend_application.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/user/orders")
public class UserOrderController {

    private final UserOrderFacade userOrderFacade;
    private final UserService userService;

    public UserOrderController(UserOrderFacade userOrderFacade, UserService userService) {
        this.userOrderFacade = userOrderFacade;
        this.userService = userService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UUID userId = userService.findIdByUsername(username);
        return userOrderFacade.searchOrders(userId, model);
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable UUID id, Model model) {
        return userOrderFacade.getOrderById(id, model);
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable UUID id) {
        return userOrderFacade.deleteOrder(id);
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable UUID id, Model model) {
        return userOrderFacade.editOrderForm(id, model);
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable UUID id,
                              @Valid @ModelAttribute("orderDetails") OrderDetailsRequest orderDetails) {
        return userOrderFacade.updateOrder(id, orderDetails);
    }

    @GetMapping("/create")
    public String getCreateOrderPage(Model model) {
        return userOrderFacade.getCreateOrderPage(model);
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute OrderItemsRequestsDTO orderItemsDTO) {
        return userOrderFacade.createOrder(orderItemsDTO);
    }
}
