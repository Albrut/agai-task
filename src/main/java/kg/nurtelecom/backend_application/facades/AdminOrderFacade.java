package kg.nurtelecom.backend_application.facades;

import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

public interface AdminOrderFacade {
    String getAllOrders(Model model);
    String getOrderById(UUID id, Model model);
    String deleteOrder(UUID id);
    String searchOrders(UUID userId, Model model);
    String editOrderForm(UUID id, Model model);
    String updateOrder(UUID id, OrderDetailsRequest orderDetails);
    String getCreateOrderPage(Model model);
    String createOrder(OrderItemsRequestsDTO orderItemsDTO);

    ResponseEntity<List<OrderResponse>> getAllOrdersRest();
    ResponseEntity<OrderResponse> getOrderByIdRest(UUID id);
    ResponseEntity<Void> deleteOrderRest(UUID id);
    ResponseEntity<List<OrderResponse>> searchOrdersRest(UUID userId);
    ResponseEntity<OrderResponse> updateOrderRest(UUID id, OrderDetailsRequest orderDetails);
    ResponseEntity<OrderResponse> createOrderRest(OrderItemsRequestsDTO orderItemsDTO);
}
