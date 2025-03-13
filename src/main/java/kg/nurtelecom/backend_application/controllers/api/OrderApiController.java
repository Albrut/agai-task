package kg.nurtelecom.backend_application.controllers.api;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.OrderFacade;
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

    private final OrderFacade orderFacade;

    public OrderApiController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return orderFacade.getAllOrdersRest();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        return orderFacade.getOrderByIdRest(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        return orderFacade.deleteOrderRest(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam UUID userId) {
        return orderFacade.searchOrdersRest(userId);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable UUID id,
            @Valid @RequestBody OrderDetailsRequest orderDetails) {
        return orderFacade.updateOrderRest(id, orderDetails);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderItemsRequestsDTO orderItemsDTO) {
        return orderFacade.createOrderRest(orderItemsDTO);
    }
}
