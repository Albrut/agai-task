package kg.nurtelecom.backend_application.controllers.api;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.AdminOrderFacade;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    private final AdminOrderFacade adminOrderFacade;

    public OrderApiController(AdminOrderFacade adminOrderFacade) {
        this.adminOrderFacade = adminOrderFacade;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return adminOrderFacade.getAllOrdersRest();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        return adminOrderFacade.getOrderByIdRest(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        return adminOrderFacade.deleteOrderRest(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam UUID userId) {
        return adminOrderFacade.searchOrdersRest(userId);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable UUID id,
            @Valid @RequestBody OrderDetailsRequest orderDetails) {
        return adminOrderFacade.updateOrderRest(id, orderDetails);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderItemsRequestsDTO orderItemsDTO) {
        return adminOrderFacade.createOrderRest(orderItemsDTO);
    }
}
