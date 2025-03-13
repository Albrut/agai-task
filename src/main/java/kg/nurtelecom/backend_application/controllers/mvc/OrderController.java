package kg.nurtelecom.backend_application.controllers.mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.OrderFacade;
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
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        return orderFacade.getAllOrders(model);
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable UUID id, Model model) {
        return orderFacade.getOrderById(id, model);
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable UUID id) {
        return orderFacade.deleteOrder(id);
    }

    @GetMapping("/search")
    public String searchOrders(@RequestParam UUID userId, Model model) {
        return orderFacade.searchOrders(userId, model);
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable UUID id, Model model) {
        return orderFacade.editOrderForm(id, model);
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(
            @PathVariable UUID id,
            @Valid @ModelAttribute("orderDetails") OrderDetailsRequest orderDetails) {

    return orderFacade.updateOrder(id, orderDetails);
    }

    @GetMapping("/create")
    public String getCreateOrderPage(Model model) {
        return orderFacade.getCreateOrderPage(model);
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute OrderItemsRequestsDTO orderItemsDTO) {
        return orderFacade.createOrder(orderItemsDTO);
    }
}
